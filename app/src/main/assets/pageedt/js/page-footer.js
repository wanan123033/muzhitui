$(function(){
	// 如果是通过分享打开的文章，就不显示编辑按钮
	if(!share){
		var stt ='<span id="bii" style="display:block;width:3rem;height:3rem;position:fixed;right:1rem;top:1rem;"><img src="/song/app/img/bii.png" width="100%" height="100%"/></span>';
		$("body").after(stt);
		$("#bii").click(function(){
			window.location.href = "/song/newPageController/toPageUpdate?id="+id;
		});
	}
	
	  // 在底部增加打赏和评论
	  var str = '<div class="reward"><p class="clearfix" style="line-height:2rem;padding:0.2rem 1rem 0;font-size:1.1rem;"><spna>阅读 '+read+'</span><span class="margin-l2"><span id="zanbuzan" class="iconfont icon-buzann" style="font-size:1.8rem;line-height:2rem;position:relative;top:0.2rem;"></span><span id="praiseCount">'+praise+'</span></span><span class="frt" style="font-size:1.1rem;position:relative;top:0.3rem;" onclick="report()">举报</span></p><div class="reward-top clearfix"><button onclick="admire();">打赏</button><p id="rewardCount"></p><ul class="user-reward">';
	  $.ajax(server+'/rewardApi/getRewards?pageId='+id,{
		dataType:'json',//服务器返回json格式数据
		type:'post',//HTTP请求类型
		//timeout:10000,//超时时间设置为10秒；
		success:function(data){
			  for(var i=0;i<data.length;i++){
				 str += '<li><img src="'+data[i].wechatimg+'"/></li>';
			  }
			  str += '</ul></div>';
		      // str+= '<div class="reward-bom clearfix"><i class="line-rew"><p><span>精彩评论</span></p></i><ul><li class="clearfix"><div class="reward-pic"><i><img src="${server}/app/img/jianjie_3.png"/></i></div><div class="reward-content"><p class="user-name">妞妞</p><p class="comment">好文采好文采好文采好文采好文采好文采好文采好文采</p><p class="date">1月8日</p></div><div class="zhan"><i class="foucs"></i><span>16</span></div></li><li class="clearfix"><div class="reward-pic"><i><img src="${server}/app/img/jianjie_3.png"/></i></div><div class="reward-content"><p class="user-name">妞妞</p><p class="comment">好文采好文采好文采好文采好文采好文采好文采好文采好文采好文采好文采好文采好文采</p><p class="date">1月8日</p></div><div class="zhan"><i class="zan"></i><span>16</span></div></li><li class="clearfix"><div class="reward-pic"><i><img src="${server}/app/img/jianjie_3.png"/></i></div><div class="reward-content"><p class="user-name">妞妞</p><p class="comment">好文采</p><p class="date">1月8日</p></div><div class="zhan"><i class="zan"></i><span>16</span></div></li><li class="clearfix"><div class="reward-pic"><i><img src="${server}/app/img/jianjie_3.png"/></i></div><div class="reward-content"><p class="user-name">妞妞</p><p class="comment">好文采</p><p class="date">1月8日</p></div><div class="zhan"><i class="zan"></i><span>166666</span></div></li></ul><i class="line-rew"><p><span>以上留言由拇指推筛选后显示</span></p></i></div></div>';
		      // 举报
		      str += '<div style="background:#fff">';
		      str += '<p style="font-size:12px;text-align: center;color:#999;line-height:20px;margin-bottom:5px;">注：文中广告由“拇指推”编辑器制作</p><div style="padding:10px 10px;background:#eee"><div style="width:80%;height: 1px;background:#ccc;margin:10px auto;position: relative;"><p style="position: absolute;width:100%;text-align: center;top:-12px;"><span style="background:#eee;padding:0 10px;font-size:14px;color:#ccc">推广</span></p></div>';
			  str += '</div>';
		      
		      //str += '<p style="font-size:12px;text-align: center;color:#999;line-height:20px;margin-bottom:5px;">注：文中广告由“拇指推”和<a href="'+server+'/app/img/jingying.jpg">“精英名商会”</a>联合推出</p><div style="padding:10px 10px;background:#eee"><div style="width:80%;height: 1px;background:#ccc;margin:10px auto;position: relative;"><p style="position: absolute;width:100%;text-align: center;top:-12px;"><span style="background:#eee;padding:0 10px;font-size:14px;color:#ccc">推广</span></p></div><img onclick="moshi();" src="'+server+'/app/img/bottom-pic.png" alt="" width="100%" height="auto"/></div>';
		      str += '</div></div>';
		      $("body").after(str);
			  $("#rewardCount").html(data.length+"人打赏");
			  // 留言点赞
		      $("#zanbuzan").click(function(){
		    	  var i = 0;
		    	  if($(this).hasClass("icon-zann")){
		    		  // 取消赞
		    		  $(this).addClass('icon-buzann');
		    		  $(this).removeClass('icon-zann');
		    		  i--;
		    	  }else{
		    		  // 赞
		    		  $(this).addClass('icon-zann');
		    		  $(this).removeClass('icon-buzann');
		    		  i++;
		    	  }
		    	  $.post(server+"/newPageController/praise",{"id":id,"i":i},function(data){
		    		  if(data.result == 1){
		    			  // 操作成功
		    			  var praiseCount = parseInt($("#praiseCount").text());
		    			  $("#praiseCount").text(praiseCount+i);
		    		  }else{
		    			  alert("操作失败");
		    		  }
		    	  });
			  });
			}
	   });
      
//	  $('.zhan i').click(function(){
//		$(this).toggleClass('foucs').toggleClass('zan');
//	  });
	  
	  // 打赏
	  var zanStr = '<div id="zanshang" class="dashangtankuang">';
	  if(zanshang && zanshang == "1"){
		  zanStr = '<div id="zanshang" class="dashangtankuang" style="display:block">';
	  }
	  zanStr += '<div class="shang"><div class="user-xinxi"><i class="all-pic user-photo"><img src="'+wechatimg+'"/></i><p>'+wechatname+'</p></div>        <div class="dashang-jine" onclick="topay(9.9)">        <p>打赏</p>        <p>        <span>￥9.</span><span>90</span>        </p>    </div>    <div class="dashang-renyi">    <div class="renyi">           任意赏        </div>    </div>    <div class="guanbis">        <span class="iconfont icon-guanbi1 guanbi1"></span>    </div></div>';
	  zanStr += '<div class="renyishang dis-no"><div class="renyishang-content"><div class="renyishang-title">任意赏 <span class="iconfont icon-iconset0415-copy fanhui0"></span>    </div>    <ul class="clearfix dashang-jines">    <li class="mui-col-xs-2 flt text-cen"><span class="iconfont icon-qian"></li>    <li class="mui-col-xs-8 flt"><input type="text" name="shangjin" id="shangjin" placeholder="输入打赏金额" value="" /></li>    <li class="mui-col-xs-2 flt text-cen"><span class="iconfont icon-jinbiline"></span></li>    </ul>    <div class="text-cen">    <button onclick="surePay()" class="queren-zhifu">确认支付</button>    <p class="beizhus">打赏无悔，概不退款</p>    </div></div></div></div>';
	  $("body").after(zanStr);
	//编辑
  	$(".xiugai").click(function(){
  		$(".tankuang").show();
  	});
  	
  	 //关闭
  	$(".guanbi").click(function(){
  		$(".tankuang").hide();
  	});
  	
  	//打赏
  	$(".dashang").click(function(){
  		$(".dashangtankuang").show();
  	});
  	//打赏关闭
  	$(".guanbis").click(function(){
  		$(".dashangtankuang").hide();
  	});
  	
  	//任意打赏
  	$(".renyi").click(function(){
  		$(".renyishang").show();
  		$(".shang").hide();
  	});
  	
  	 //返回
  	$(".fanhui0").click(function(){
  		$(".renyishang").hide();
  		$(".shang").show();
  	});
  	
});

//跳转到举报页面
function report(){
	window.location.href = server+"/app/report.jsp?id="+id;
}
// 确认支付
function surePay(){
	var money = $("#shangjin").val();
	var a=/^[0-9]*(\.[0-9]{1,2})?$/;
	if(!a.test(money)){
		alert("请输入正确金额");
		$("#shangjin").val("");
		return false;
	}
	if(!money || parseFloat(money) < 1){
		alert("请最少赞赏1元钱");
		$("#shangjin").val("");
		return false;
	}
	if(parseFloat(money) > 256){
		$("#shangjin").val("");
		alert("每次最多只能打赏256元钱");
		return false;
	}
	topay(money);
}

// 显示打赏
function admire(){
	window.location.href = server+"/newLoginController/getCode?flag=5&pageId="+id;
}
// 打赏支付
function topay(money){
	// 生成订单
	$.ajax(server+'/rewardApi?reward.pageinterim_id='+id+'&reward.login_id='+loginId+'&reward.money='+money,{
		dataType:'json',
		type:'json',
		//data:str,
		timeout:10000,
		success:function(data){
			if(data.flag==0){
				//去支付
				window.location.href=server+'/rewardApi/toPay?ordernum='+data.ordernum;
			}else{
  				// 恢复样式
				alert("打赏失败");
			}
		}
	})
}