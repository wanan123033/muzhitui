$(function(){
	var filechooser = document.getElementById("choose");
	//  用于压缩图片的canvas
	var canvas = document.createElement("canvas");
	var ctx = canvas.getContext('2d');
	//  瓦片canvas
	var tCanvas = document.createElement("canvas");
	var tctx = tCanvas.getContext("2d");
	var maxsize = 100 * 1024;
	filechooser.onchange = function () {
	  if (!this.files.length) return;
	  var files = Array.prototype.slice.call(this.files);
	  if (files.length > 1) {
	      alert("最多同时只可上传1张图片");
	      return;
	  }
	  files.forEach(function (file, i) {
	      if (!/\/(?:jpeg|png|gif)/i.test(file.type)) return;
	      var reader = new FileReader();
	      var li = document.createElement("li");
	      // 获取图片大小
	      reader.onload = function () {
	          var result = this.result;
	          var img = new Image();
	          img.src = result;
	          $(li).css("background-image", "url(" + result + ")");
	          // 如果图片大小小于100kb，则直接上传
	          if (result.length <= maxsize) {
	              img = null;
	              upload(result, file.type, $(li));
	              return;
	          }
	          // 图片加载完毕之后进行压缩，然后上传
	          if (img.complete) {
	              callback();
	          } else {
	              img.onload = callback;
	          }
	          function callback() {
	              var data = compress(img);
	              upload(data, file.type, $(li));
	              img = null;
	          }
	      };
	      reader.readAsDataURL(file);
	  })
	};
	//  使用canvas对大图片进行压缩
	function compress(img) {
	  var initSize = img.src.length;
	  var width = img.width;
	  var height = img.height;
	  //如果图片大于四百万像素，计算压缩比并将大小压至400万以下
	  var ratio;
	  if ((ratio = width * height / 4000000)>1) {
	      ratio = Math.sqrt(ratio);
	      width /= ratio;
	      height /= ratio;
	  }else {
	      ratio = 1;
	  }
	  canvas.width = width;
	  canvas.height = height;
	//      铺底色
	  ctx.fillStyle = "#fff";
	  ctx.fillRect(0, 0, canvas.width, canvas.height);
	  //如果图片像素大于100万则使用瓦片绘制
	  var count;
	  if ((count = width * height / 1000000) > 1) {
	      count = ~~(Math.sqrt(count)+1); //计算要分成多少块瓦片
	//          计算每块瓦片的宽和高
	      var nw = ~~(width / count);
	      var nh = ~~(height / count);
	      tCanvas.width = nw;
	      tCanvas.height = nh;
	      for (var i = 0; i < count; i++) {
	          for (var j = 0; j < count; j++) {
	              tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh);
	              ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh);
	          }
	      }
	  } else {
	      ctx.drawImage(img, 0, 0, width, height);
	  }
	  //进行最小压缩
	  var ndata = canvas.toDataURL('image/jpeg', 0.1);
	  tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0;
	  return ndata;
	}
	//  图片上传，将base64的图片转成二进制对象，塞进formdata上传
	function upload(basestr, type, $li) {
	  var text = window.atob(basestr.split(",")[1]);
	  var buffer = new Uint8Array(text.length);
	  var pecent = 0 , loop = null;
	  for (var i = 0; i < text.length; i++) {
	      buffer[i] = text.charCodeAt(i);
	  }
	  var blob = getBlob(buffer, type);
	  var xhr = new XMLHttpRequest();
	  var formdata = new FormData();
	  formdata.append('imagefile', blob);
	  xhr.open('post', '/song/fileUploadApi/compressUpload');
	  xhr.onreadystatechange = function () {
	     if (xhr.readyState == 4 && xhr.status == 200) {
	    	var jsonData = JSON.parse(xhr.responseText);
	    	$("#adimage").val(jsonData.imgUrl);
			$("#imgtext").val("已上传");
			$("#topimg").attr("src","/song/"+jsonData.imgUrl);
			$("#topimg").attr("style","");
		}
	  };
	  //数据发送进度，前50%展示该进度
	  xhr.upload.addEventListener('progress', function (e) {
	      if (loop) return;
	      pecent = ~~(100 * e.loaded / e.total) / 2;
	      $li.find(".progress span").css('width', pecent + "%");
	      if (pecent == 50) {
	          mockProgress();
	      }
	  }, false);
	  //数据后50%用模拟进度
	  function mockProgress() {
	      if (loop) return;
	      loop = setInterval(function () {
	          pecent++;
	          $li.find(".progress span").css('width', pecent + "%");
	          if (pecent == 99) {
	              clearInterval(loop);
	          }
	      }, 100)
	  }
	  xhr.send(formdata);
	}
	//获取blob对象的兼容性写法
	function getBlob(buffer, format){
	  var Builder = window.WebKitBlobBuilder || window.MozBlobBuilder;
	  if(Builder){
	      var builder = new Builder;
	      builder.append(buffer);
	      return builder.getBlob(format);
	  } else {
	      return new window.Blob([ buffer ], {type: format});
	  }
	}
})