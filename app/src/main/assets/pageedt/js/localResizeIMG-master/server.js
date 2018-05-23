document.querySelector('#choose').addEventListener('change', function () {
	$("progress").show();
    var that     = this,
        progress = document.querySelector('progress');

    lrz(that.files[0], {
        width: 800
    })
        .then(function (rst) {
            progress.value = 0;
            /* ==================================================== */
            // 原生ajax上传代码
            var xhr = new XMLHttpRequest();
            xhr.open('POST', '/song/fileUploadApi/compressUpload');
            xhr.onload = function () {
                var data = JSON.parse(xhr.response);
                if (xhr.status === 200) {
                    // 上传成功
                	var jsonData = JSON.parse(xhr.responseText);
                	$("#adimage").val(jsonData.imgUrl);
        			$("#imgtext").val("已上传");
                	
                	document.querySelector('#topimg').src = rst.base64;
                	$('#topimg').attr("style","");
                    progress.value = 0;
                    $('progress').hide();
                } else {
                    // 处理错误
                    alert(data.msg);
                    that.value = null;
                }
            };

            xhr.onerror = function (err) {
                alert('未知错误:' + JSON.stringify(err, null, 2));
                that.value = null;
            };
            // issues #45 提到似乎有兼容性问题,关于progress
            if (xhr.upload) {
                try {
                    xhr.upload.addEventListener('progress', function (e) {
                        if (!e.lengthComputable) return false;

                        // 上传进度
                        progress.value = ((e.loaded / e.total) || 0) * 100;
                    });
                } catch (err) {
                    console.error('进度展示出错了,似乎不支持此特性?', err);
                }
            }
            // 添加参数
            rst.formData.append('fileLen', rst.fileLen);
            rst.formData.append('xxx', '我是其他参数');
            // 触发上传
            xhr.send(rst.formData);
            return rst;
        });
});