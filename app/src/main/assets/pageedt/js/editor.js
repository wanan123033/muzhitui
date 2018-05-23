var i = 0;
var website = '11'; // 原文网址
var image = '11'; // 原文首图
var id = '11'; // 编辑后的文章id
$(function () {
    // 移除所有a的href，但是不包括拇指推里的页眉和广告链接
    $("a").each(function (index, obj) {
        if (!$(this).hasClass("songAdv")) {
            $(this).removeAttr("href");
        }
    });
    //body前后加一个div
//	  var ss = '<div style="height:3rem;background:#fff;position: fixed;width:100%;left:0;top:6rem;right:0;bottom:0;z-index: 999;">12355</div>';
//	  $("body").append(ss);
    var e = $("body *:not(span)");
    var loading = '<div class="loading-mc" style="display: none"><span style=""><i class="dis-b loading mar-auto"><img src="img/app/loading.gif" alt="" /></i><p class="text-cen">正在读取...</p></span></div>';
    $("body").after(loading);
    //
    // var show = '<div class="show" id="show" style="display: none"><ul class="show0"><li class="addfont0"><img src="img/app/wenzi.png" width="100%" height="100%"/></li><li class="guanggao0"><img src="img/app/guanggao.png" width="100%" height="100%"/></li><li class="shangchuan0"><img src="img/app/tupian.png" width="100%" height="100%"/><input type="file" name="photo" id="photo" accept="image/!*" value="上传"/></li><li class="shipin0"><img src="img/app/shiping.png" width="100%" height="100%"/></li><li class="yinyue0"><img src="img/app/yinyue.png" width="100%" height="100%"/></li><li class="shuanchu0"><img src="img/app/shanchu.png" width="100%" height="100%"/></li><li class="shuanqian0"><img src="img/app/shanqian.png" width="100%" height="100%"/></li><li class="shuanhou0"><img src="img/app/shanhou.png" width="100%" height="100%"/></li><i class="all-pic quxiao0" style="width:30px;height:30px;position:absolute;right:3px;top:-13px;"><img src="img/app/chacha.png" /></i></ul><div class="editor"><ul><li class="clearfix title-bvox"><div class="qingkong">清空</div><div class="quxiao">×</div></li><li><textarea name="font" rows="6" id="textarea" class="textarea" cols=""></textarea></li><li class="title-bvox clearfix"><div class="type">样式</div><div class="add">完成</div></li></ul></div><div class="typelist clearfix" style="display: none;"><div class="line-sj"></div><ul class="tlt clearfix"><li class="foucs tt"><div>大小</div></li><li class="tt"><div>颜色</div></li><li class="tt"><div>对齐</div></li><li class="tt"><div>动画</div></li></ul> <ul class="cont"><li class="clearfix font-size asdd"><div><span class="bigsize">最大</span></div><div><span class="middlesize size-m">中等</span></div><div><span class="smallsize">最小</span></div></li><li class="color clearfix asdd" style="display: none;"><ul><li class="bg0" id="color1"></li><li class="bg1" id="color2"></li><li class="bg2" id="color3"></li><li class="bg3" id="color4"></li><li class="bg4" id="color5"></li><li class="bg5" id="color6"></li><li class="bg6" id="color7"></li><li class="bg7" id="color8"></li><li class="bg8" id="color9"></li><li class="bg9" id="color10"></li></ul></li><li class="text-al clearfix asdd" style="display: none;"><ul class="font-14 all-pic clearfix"><li class="text-lt left clearfix"><i class="clearfix"><img src="img/app/left-t.png"/></i><p>左对齐</p></li><li class="text-cen center clearfix"><i class="clearfix"><img src="img/app/center-t.png"/></i><p>居中</p></li><li class="text-rt right clearfix"><i class="clearfix"><img src="img/app/right-t.png"/></i><p>右对齐</p></li></ul></li><li class="animent all-pic asdd" style="display: none;"><div class="donghua"><i class="clearfix"><img src="img/app/animent-l.png" alt="" /></i></div><div class="donghua"><i class="clearfix"><img src="img/app/animent-l.png" alt="" /></i></div></li></ul></div></div>';
    /*	$("body").after(show);*/
    var show = '<div class="show" id="show" style="display: none"> <ul class="show0" style="display: none"> <li class="addfont0"><img src="img/newpage/font.png" width="100%" height="100%"/></li> <li class="guanggao0"><img src="img/newpage/addAd.png" width="100%" height="100%"/></li> <li class="shangchuan0"><img src="img/newpage/addImg.png" width="100%" height="100%"/> <input type="file" name="photo" id="photo" accept="image/*" value="上传"/></li> <li class="yinyue0"><img src="img/newpage/addMusic.png" width="100%" height="100%"/></li> <li class="shuanchu0"><img src="img/newpage/del.png" width="100%" height="100%"/></li><li class="shuanqian0"><img src="img/newpage/delAfter.png" width="100%" height="100%"/></li><li class="shuanhou0"><img src="img/newpage/delBefore.png" width="100%" height="100%"/></li> <li class="revoke"><img src="img/newpage/revoke.png" width="100%" height="100%"/></li> <i class="all-pic quxiao0" style="width:30px;height:30px;position:absolute;right:3px;top:-13px;"><img src="img/app/chacha.png"/></i></ul> <div class="typelist clearfix" style="display: none"> <ul class="tlt clearfix"> <li class=" foucs tt"> <div>对齐</div> </li> <li class=" tt"> <div>颜色</div> </li> <li class=" tt"> <div>大小</div> </li> <li class=" tt"> <div>字体</div> </li> <li class=" tt"> <div>动画</div> </li> </ul> <ul class="cont"> <li class="clearfix font-size asdd" style=" display: inline-block; "> <div style="width: 100px;float: left"><img width="41px" height="41px" style="position:absolute;left:10px;top:10px" src="img/newpage/select_ct.png"/> <img width="20" style="position:absolute;left:20px;top:20px" src="img/newpage/align-left.png"/> <p style="position:absolute;left:10px;top:60px">左对齐</p></div> <div style="width: 100px;float: left"><img width="41px" height="41px" style="position:absolute;left:10px;top:10px" src="img/newpage/select_ct.png"/> <img width="20" style="position:absolute;left:20px;top:20px" src="img/newpage/align-center.png"/> <p style="position:absolute;left:10px;top:60px">&nbsp;居中</p></div> <div style="width: 100px;float: left"><img width="41px" height="41px" style="position:absolute;left:10px;top:10px" src="img/newpage/select_ct.png"/> <img width="20" style="position:absolute;left:20px;top:20px" src="img/newpage/align-right.png"/> <p style="position:absolute;left:10px;top:60px">右对齐</p></div> </li> <li class="clearfix" style="height: 100%;display: none;"> <div style="    display:inline-block; width: 100%;height: 100%;padding: 0; overflow-:auto; overflow-y: hidden"> <span style="width: 6%; background-color:#000000;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#FFFFFF;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#545454;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#a8a8a8;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#3fe2c5;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#00ab98;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#00997b;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#0c9466;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#00ab65;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#7ec044;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#b7da8a;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#f8e000;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#dcb30b;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#b28202;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#a3701f;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#cc6f21;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#a3701f;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#f97f2c;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#fc552b;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#ee332a;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#f48ebd;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#f075ab;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#fd43a8;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#c928b2;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#9521c0;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#7748f6;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#6465fe;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#2f47b1;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#0161b8;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#017cbf;height: 100%;display: inline-block;float: left"></span> <span style="width: 6%; background-color:#69adde;height: 100%;display: inline-block;float: left"></span> </div> </li> <li class=" all-pic asdd" style="margin: 0 auto;float: none;padding-top: 30px;display: none;"> <input type="range" name="points" min="10" max="50" step="1" value="10"/> </li> <li class="clearfix font-size asdd" style=" display: inline-block;padding-bottom: 20px;display: none;"> <div style="width: 80px;float: left"><img width="41px" height="41px" style="position:absolute;left:10px;top:10px" src="img/newpage/select_ct.png"/> <img width="20" style="position:absolute;left:20px;top:20px" src="img/newpage/font_s.png"/> <p style="position:absolute;left:10px;top:60px">&nbsp;宋体</p></div> <div style="width: 80px;float: left"><img width="41px" height="41px" style="position:absolute;left:10px;top:10px" src="img/newpage/select_ct.png"/> <img width="20" style="position:absolute;left:20px;top:20px" src="img/newpage/font_fs.png"/> <p style="position:absolute;left:10px;top:60px">&nbsp;仿宋</p></div> <div style="width: 80px;float: left"><img width="41px" height="41px" style="position:absolute;left:10px;top:10px" src="img/newpage/select_ct.png"/> <img width="20" style="position:absolute;left:20px;top:20px" src="img/newpage/font_ht.png"/> <p style="position:absolute;left:10px;top:60px">&nbsp;黑体</p></div> <div style="width: 80px;float: left"><img width="41px" height="41px" style="position:absolute;left:10px;top:10px" src="img/newpage/select_ct.png"/> <img width="20" style="position:absolute;left:20px;top:20px" src="img/newpage/font_ks.png"/> <p style="position:absolute;left:10px;top:60px">&nbsp;隶书</p></div> <div style="width: 80px;float: left"><img width="41px" height="41px" style="position:absolute;left:10px;top:10px" src="img/newpage/select_ct.png"/> <img width="20" style="position:absolute;left:20px;top:20px" src="img/newpage/font_ch.png"/> <p style="position:absolute;left:10px;top:60px">&nbsp;粗黑</p></div> </li> <li class=" all-pic asdd" style="display: inline-block;margin: 0 auto;float: none;display: none"> <div class="donghua" style="width: 80px"><i class="clearfix"><img src="img/newpage/animation_enlarge.png" alt=""/></i></div> <div class="donghua" style="width: 80px"><i class="clearfix"><img src="img/newpage/animation_fade.png" alt=""/></i></div> <div class="donghua" style="width: 80px"><i class="clearfix"><img src="img/newpage/animation_narrow.png" alt=""/></i></div> <div class="donghua" style="width: 80px"><i class="clearfix"><img src="img/newpage/animation_u_d_jitter.png" alt=""/></i></div> <div class="donghua" style="width: 80px"><i class="clearfix"><img src="img/newpage/animation_z_y_jitter.png" alt=""/></i></div> </li> </ul> </div> <div class="editor" style="display: none"> <ul> <li class="clearfix title-bvox" style="width: 100%;display: none"> <div class="quxiao"><img width="30px" src="img/newpage/close.png"/></div> <div class="qingkong"><p>清空</p></div> <div class="type"><p style="color:#ffd800">样式</p></div> <div class="confirm"><img width="30px" src="img/newpage/comf.png"/> &nbsp;</div> </li> <li><textarea name="font" rows="6" id="textarea" class="textarea" cols=""></textarea></li> <li class="title-bvox clearfix" style="height: 7px"></li> </ul> </div> </div>';
    $("body").after(show);
    var here = $(".tlt li.tt");					//编辑器title切换
    var oDiv = $('.cont li');
    var i = 0;
    for (i = 0; i < here.length; i++) {
        here[i].index = i;
        here[i].onclick = function (ev) {
            for (i = 0; i < here.length; i++) {
                here.removeClass('foucs');
                oDiv[i].style.display = "none";
            }
            $(this).addClass('foucs');
            if (this.index == 0 || this.index == 3 || this.index == 4) {
                oDiv[this.index].style.display = "inline-block";
            } else {
                oDiv[this.index].style.display = "block";
            }

            return false;
        };
    }
    ;

    // 安卓，ios暂时用不同的上传图片方法
    if (browser.versions.android) {
        var v = '';
        v += '<div class="over">';
        v += '  <input type="hidden" name="hidf" id="hidf" value="" />';
        v += '</div>';
        v += '<div class="title" style="padding:0 0.5rem;">';
        v += '<div style="margin-top:10rem">';
        v += '   <div class="flt" style="width:41.6%"><div style="width:8rem;height:8rem;margin:0 auto;position:relative;ovfllow:hidden">';
        // 如果做添加，缩略图就显示为logo
        if (null != id && id != "") {
            v += '<input id="sImg" type="hidden" value="' + image + '" /><img id="smallImg" src="/song/' + image + '" width="100%" height="100%" /><input id="smallUpload" type="file" name="file" accept="image/*" value="" style="position:absolute;width:100%;height:100%;top:0;left:0;right:0;bottom:0;z-index:20; filter:alpha(opacity=0);opacity:0;"/>';
        } else {
            v += '<input id="sImg" type="hidden" value="/wx/img/logo.jpg" /><img id="smallImg" src="/song/wx/img/logo.jpg" width="100%" height="100%" /><input id="smallUpload" type="file" name="file" accept="image/*" value="" style="position:absolute;width:100%;height:100%;top:0;left:0;right:0;bottom:0;z-index:20; filter:alpha(opacity=0);opacity:0;"/>';
        }
        v += '    	</div></div>';
        v += '     	<div class="flt" style="width:58.4%;padding-top:0.3rem;">';
        v += '        	<h3 style="line-height:2.2rem;font-size:1.6rem;font-weight: normal;margin:0;text-align: left; ">标题：</h3>';
        v += '        <div>';
        v += '     	<textarea rows="2" style="padding:0.5rem;border:1px #f1f1f1 solid;" name="title" id="title" ></textarea>';
        v += '   </div>';
        v += '</div>';
        v += '</div>';
    } else {
        var v = '';
        v += '<div id="yuan-box">';
        v += '	<div class="over">';
        v += '  		<input type="hidden" name="hidf" id="hidf" value="" />';
        v += '	</div>';
        v += '	<div class="title" style="padding:10rem 0.5rem 0;">';
        v += '	<div>';
        v += '    <div id="smallUpload" class="flt" style="width:30%;padding-left:5%;"><div style="width:8rem;height:8rem;position:relative;display:table-cell;text-align:center;vertical-align:middle;border:1px #ddd solid"><i class="all-pic" style="position:absolute;width:2rem;height:2rem;right:0;bottom:0;"><img src="img/app/paizhao.png" /></i>';
        // 如果做添加，缩略图就显示为logo
        if (null != id && id != "") {
            v += '<input id="sImg" type="hidden" value="' + image + '" /><img id="showImg" src="/song/' + image + '" style="max-width:8rem;max-height:8rem;display:block;margin:0 auto;" />';
        } else {
            v += '<input id="sImg" type="hidden" value="/wx/img/logo.jpg" /><img id="showImg" src="/song/wx/img/logo.jpg" style="max-width:8rem;max-height:8rem;display:block;margin:0 auto;" />';
        }
        v += '    </div></div>';
        v += '    <div class="flt clearfix" style="width:65%;height:8rem;padding-top:0.3rem;position:relative">';
        v += '        <h3 class="text-left" style="font-size:1.6rem;font-weight: normal;margin:0;position:absolute;top:0;left:0;">标题：</h3>';
        v += '    	<div style="position:absolute;bottom:0;left:0;">';
        v += '    		<textarea cols="25" rows="3" style="padding:0.5rem;border:1px #f1f1f1 solid;font-size:1.1rem;width:100%;" name="title" id="title" ></textarea>';
        v += '		</div>';
        v += '	</div></div></div></div>';
        v += '<div id="pic-boox" style="display:none">'
        v += '<div class="lazy_tip" id="lazy_tip"><span>1%</span><br>	载入中......</div>';
        v += '<div id="clipArea" style=""></div>';
        v += '<button id="upload2">取消上传</button>';
        v += '<button id="clipBtn">确定上传</button>';
        v += '<input type="file" id="file" style="opacity: 0;position: fixed;bottom: -100px">';
        //v += ' <div id="plan" style="display:none">比例剪切后尺寸<canvas id="myCanvas"></canvas></div>';
        v += '<img src="" fileName="" id="hit" style="display:none;z-index: 9">';
        v += '</div>';
    }
    $("body").after(v);

    var tilte = $("title").html();
    $("textarea[name=title]").val(tilte);
    var w = $("body").width();
    e.click(function sdf(event) {
        $(this).addClass("dddddd");
        var xx = $("#hidf").val();
        $(".over").show();
        // $(".show").css("width",w);
        $(".textarea").val('');
        $(".editorgao").remove();
        $(".bg").css("background", xx);
        var bg = $(this).css("background");
        $("#hidf").val(bg);
        $("body *:not(span)").removeClass("bg");
        $(this).addClass("bg");
        $(".bg").after("<div style='width:100%;' class='editorgao'></div>");
        $(".editorgao").css("height", "10rem");
        $(this).css("background", "#00ffff");
        $("#show").hide().show();

        $("#show").insertAfter($(this));
//	 	var w2 = $(".bg").parent().offset().left;
//	 	$(".show").css("margin-left",-w2);
        $(".show0").show();
        $(".typelist").hide();
        //$(".editor").hide();
        event.stopImmediatePropagation();
    });

    $(".shangchuan0").click(function (event) {
        $(".editorgao").remove();
        event.stopImmediatePropagation();
        WebViewJavascriptBridge.callHandler(
            'choiceImg'
            , {'param': ""}
            , function (responseData) {
                $(".bg").after('<div class="shangchuanimg"><img class="pageimages" src="' + baseUrl + responseData + '" width="100%" /></div>');
                $("#show").hide();
                var xx = $("#hidf").val();
                $(".bg").css("background", xx);
                $("body *:not(span)").removeClass("bg");
                $(".shangchuanimg").click(function (event) {
                    $(".textarea").val('');
                    $(this).addClass("bg").siblings().removeClass("bg");
                    $("#show").show();
                    $("#show").insertAfter($(this));
                    $(".show0").show();
                    $(".editor").find("li").eq(0).hide();
                    $(".editor").hide();

                    event.stopImmediatePropagation();
                });

            }
        );
    });
    $("body").append('<div id="zhanting" class="zhanting"><img src="img/app/music.png" class="bofang" /></div>');
    //添加长条音乐
//    $("body").append('<div class="musicContent clearfix"><a class="music mcPlay" onclick="plays();"></a> <label>月亮代表我的心</label></div>');
    $(".yinyue0").click(function (event) {
        event.stopImmediatePropagation();
        WebViewJavascriptBridge.callHandler(
            'choiceMusic'
            , {'param': ""}
            , function (responseData) {
                $(".audio").remove();
                var url = null;
                if(responseData.indexOf("http") == 0){
                    url = responseData
                }else{
                    url = baseUrl + responseData
                }
                $("body").append('<audio id="music" class="audio" autoplay="autoplay" loop="loop"><source class="gequ" src="' + url + '" type="audio/mpeg"></audio>');
                $("body").show();
                var h = $(".bg").offset().top - 100;
                $(window).scrollTop(h);
                var xx = $("#hidf").val();
                $(".editorgao").remove();
                $(".bg").css("background", xx);
                $("body *:not(span)").removeClass("bg");
                $(".over").show();
                $(".show").hide();
                $(".zhanting").show();
                event.stopImmediatePropagation();

            }
        );
    });


    //取消
    $(".quxiao0").click(function (event) {
        $(".show").hide();
        $(".editorgao").remove();
        var xx = $("#hidf").val();
        $(".bg").css("background", xx);
        $("body *:not(span)").removeClass("bg");
        event.stopImmediatePropagation();
    });
    //插文字
    $(".addfont0").click(function (event) {
        $("#textarea").removeClass("wori");
        var content0 = $(".bg").html();
        var content1 = content0.replace(/<.*?>/ig, "");
        var content = content1.replace(/&nbsp;/ig, "");
        WebViewJavascriptBridge.callHandler(
            'modifyText'
            , {'param': content}
            , function (responseData) {

            }
        );
        $(".show").hide();
        event.stopImmediatePropagation();

    });

    //广告
    $(".guanggao0").click(function (event) {
        event.stopImmediatePropagation();
        WebViewJavascriptBridge.callHandler(
            'choiceAd'
            , {'param': ""}
            , function (responseData) {
                var data = jQuery.parseJSON(responseData);
                var tab = data.table;
                switch (tab) {
                    case "top":
                        addTop(data);
                        break;
                    case "bot":
                        addBottom(data);
                        break;
                    case "adv":
                        addAd(data);
                        break;
                }

            }
        );
    });


    //清空
    $(".qingkong").click(function (event) {
        $(".textarea").val('');
        event.stopImmediatePropagation();
    });
    $(".textarea").click(function (event) {
        $(".editor").show();
        $(".editor").find("li").eq(0).show();
        event.stopImmediatePropagation();
    });


    //样式
    var width = $("body").width();
    $(".type").click(function (event) {
        $(".typelist").show();
//     	$("body").hide();
//     	$(".show").hide();
//     	$(".font-size li").removeClass("border-ff");
//     	$(".text-al li").removeClass("border-ff");
//     	$(".textarea").removeClass("runingleft");
//     	$(".textarea").removeClass("runingtop");
//     	$(".textarea").css("width",width);
//     	$(".editorgao").css("height","320px");
        event.stopImmediatePropagation();
    });

    //字体大小
    //最大
    $(".bigsize").click(function (event) {
        $(".textarea").addClass("bigsize");
        $(".textarea").removeClass("smallsize");
        $(".textarea").removeClass("middlesize");
        $(".bg").removeClass("smallsize");
        $(".bg").removeClass("middlesize");
        event.stopImmediatePropagation();
    });

    //一般
    $(".middlesize").click(function (event) {
        $(".textarea").addClass("middlesize");
        $(".textarea").removeClass("bigsize");
        $(".textarea").removeClass("smallsize");
        $(".bg").removeClass("bigsize");
        $(".bg").removeClass("smallsize");
        event.stopImmediatePropagation();
    });

    //最小
    $(".smallsize").click(function (event) {
        $(".textarea").addClass("smallsize");
        $(".textarea").removeClass("bigsize");
        $(".textarea").removeClass("middlesize");
        $(".bg").removeClass("bigsize");
        $(".bg").removeClass("middlesize");
        event.stopImmediatePropagation();
    });

    //颜色
    $(".color li").click(function (event) {
        for (var k = 0; k < 10; k++) {
            $(".color li").removeClass("coloo");
        }
        $(this).addClass("coloo");
        var index = $(this).index();
        for (var k = 0; k < 10; k++) {
            if (index == k) {
                $(".textarea").addClass("color" + k);
            } else {
                $(".textarea").removeClass("color" + k);
            }
        }
        event.stopImmediatePropagation();
    });
    //对齐方式
    $(".text-al li").click(function (event) {
        $(this).addClass("border-ff").siblings().removeClass("border-ff");
        var index = $(this).index();
        if (index == 1) {
            $(".textarea").removeClass("text-rt");
            $(".textarea").removeClass("text-lt");
            $(".textarea").addClass("text-cen");
            $(".bg").removeClass("text-rt");
            $(".bg").removeClass("text-lt");
        }
        if (index == 2) {
            $(".textarea").removeClass("text-cen");
            $(".textarea").removeClass("text-lt");
            $(".textarea").addClass("text-rt");
            $(".bg").removeClass("text-cen");
            $(".bg").removeClass("text-lt");
        }
        if (index == 0) {
            $(".textarea").removeClass("text-cen");
            $(".textarea").removeClass("text-rt");
            $(".textarea").addClass("text-lt");
            $(".bg").removeClass("text-cen");
            $(".bg").removeClass("text-rt");
        }
        event.stopImmediatePropagation();
    });
    //滚动方式
    //向左滚动
    //向右滚动
    /*	$(".donghua").click(function(){
     $(this).addClass("border-ff").siblings().removeClass("border-ff");
     var index = $(this).index();
     if(index==1){
     $(".dddddd").removeClass("runingright");
     $(".dddddd").addClass("runingleft");
     }
     if(index==0){
     $(".dddddd").removeClass("runingleft");
     $(".dddddd").addClass("runingright");
     }
     event.stopImmediatePropagation();
     });*/
//
    $(".typelist").click(function (event) {
        return false;
    });
    //确定
    $(".queding").click(function (event) {
        $(".typelist").hide();
        $(".show").show();
        $("body").show();
        var h = $(".bg").offset().top - 100;
        $(window).scrollTop(h);
        event.stopImmediatePropagation();
    });
    var deli = 1;
    //删除
    $(".shuanchu0").click(function (event) {
        //$(".bg").remove();
        //$(".editorgao").remove();
        //$("body *:not(span)").removeClass("bg");
        $(".bg").addClass("delete_class_" + (deli)).hide();
        $(".editorgao").addClass("delete_class_" + (deli)).hide();
        deli++;
        var xx = $("#hidf").val();
        $(".bg").css("background", xx);
        $(".show").hide();
        event.stopImmediatePropagation();
    });


    //删前
    $(".shuanqian0").click(function (event) {
        var b = $(".bg");
        var l = $("body *:not(span)").index(b);
        $("body *:not(span)").slice(0, l - 1).addClass("biaoji");
        $(".biaoji").each(function () {
            if (!b.parents().is($(this))) {
                if (!$(this).is(":hidden")) {
                    $(this).addClass("delete_class_" + deli).hide();
                }
            }
        });
        deli++;
        $("body *:not(span)").removeClass("biaoji");
        event.stopImmediatePropagation();
    });
    //删后
    $(".shuanhou0").click(function (event) {
        var c = $(".type");
        var z = $("body *:not(span)").index(c);
        $("body *:not(span)").slice(z + 1).addClass("biaoji");
        $(".biaoji").each(function () {
            if (!$(this).is(":hidden")) {
                $(this).addClass("delete_class_" + deli).hide();
            }
        });
        deli++;
        $("body *:not(span)").removeClass("biaoji");
        event.stopImmediatePropagation();
    });

    //撤销
    $(".revoke").click(function (event) {
        $(".delete_class_" + (deli - 1)).show();
        $(".delete_class_" + (deli - 1)).removeClass("delete_class_" + (i - 1));
        deli--;
        event.stopImmediatePropagation();
    });

    $(".shipin0").click(function (event) {
        $(".textarea").css("width", "100%").addClass("wori");
        $(".show0").hide();
        $(".editor").show();
        $(".editor").find("li").eq(0).show();
        event.stopImmediatePropagation();
    });


    //退出/取消
    $(".overquxiao").click(function (event) {					//退出（取消提示框）
        $(".over").hide();
        var str = '';
        str += '<div id="sureBox" class="zhao" style="position: fixed;width:100%;height: 100%;left:0;top:0;right:0;bottom:0;background: rgba(0,0,0,0.5);z-index: 999;">';
        str += '   <div class="tuichu">';
        str += '     <center class="box-title">温馨提示</center>';
        str += '     <div class="text">';
        str += '        <center>即将退出，需要保存到草稿箱吗？</center>';
        str += '        <div class="clearfix">';
        str += '           <p><button class="querenbaocun" onclick="save(0)">保存</button></p><p><button class="fangqi">放弃</button></p>';
        str += '        </div>';
        str += '     </div>';
        str += '   </div>';
        str += '</div>';
        $("head").after(str);
        // 确认保存
        /*$(".querenbaocun").click(function(event){
         history.go(-1);
         event.stopImmediatePropagation();
         });*/
        //放弃
        $(".tuichu").click(function (ev) {
            return false;
        })
        $("#sureBox").click(function () {
            $("#sureBox").hide();
            $(".over").show();
        })
        $(".fangqi").click(function (event) {
            history.go(-1);
            event.stopImmediatePropagation();
        });
        event.stopImmediatePropagation();
    });


    //暂停/播放 $(".zhanting")
    $("#zhanting").click(function (event) {
        var music = document.getElementById("music");
        if (music.paused) {
            music.play();
            $(this).find("img").addClass("bofang");
        }
        else {
            music.pause();
            $(this).find("img").removeClass("bofang");
        }
        event.stopImmediatePropagation();
    });


    //$(".save").click(function(event){});

    //版权
    /*var banquan = "<div class='banquan' style='font-size:1.5rem;line-height:2.5rem;color:#f00;background:#fff;text-align:center;padding:20px 0;letter-spacing:0.3rem'>如有侵权，请联系管理员<br/>予以删除!!!</div>";
     $("body").after(banquan);*/

    //视频大小
//   var sss = $("iframe body").length;alert(sss);

    // 安卓，ios暂时用不同的上传图片方法
    if (browser.versions.android) {
        $('#smallUpload').bind('change', function () {
            var that = this;
            lrz(that.files[0], {
                width: 800
            })
                .then(function (rst) {
                    /* ==================================================== */
                    // 原生ajax上传代码
                    var xhr = new XMLHttpRequest();
                    xhr.open('POST', '/weixin/upload/img');
                    xhr.onload = function () {
                        var data = JSON.parse(xhr.response);
                        if (xhr.status === 200) {
                            // 上传成功

                            var jsonData = JSON.parse(xhr.responseText);
                            if (jsonData.code != 1) {
                                alert(code.msg);
                                return;
                            }
                            $(".loading-mc").hide();
                            $("#sImg").val(jsonData.imgUrl);
                            $("#smallImg").attr("src", "upload/" + jsonData.data.url);
                        } else {
                            // 处理错
                            that.value = null;
                        }
                    };

                    xhr.onerror = function (err) {
                        alert('未知错误:' + JSON.stringify(err, null, 2));
                        that.value = null;
                    };
                    // 添加参数
                    rst.formData.append('fileLen', rst.fileLen);
                    rst.formData.append('xxx', '我是其他参数');
                    // 触发上传
                    xhr.send(rst.formData);
                    return rst;
                });
        });
    } else {
        /** 上传图片开始 */
        var hammer = '';
        var currentIndex = 0;
        var body_width = $('body').width();
        var body_height = $('body').height();
        $("#clipArea").photoClip({
            width: body_width * 0.4125,
            height: body_width * 0.4125,
            file: "#file",
            view: "#hit",
            ok: "#clipBtn",
            loadStart: function () {
                //console.log("照片读取中");
                $('.lazy_tip span').text('');
            },
            loadComplete: function () {
                //console.log("照片读取完成");
                $("#yuan-box").hide();
                $("#pic-boox").show();
                $("#clipArea").show();
                $('.lazy_cover,.lazy_tip').hide();
            },
            clipFinish: function (dataURL) {
                $('#hit').attr('src', dataURL);
                saveImageInfo();
            }
        });
        // 确定上传
        $("#smallUpload").click(function () {
            $('#file').click();
        })
        //图片上传
        function saveImageInfo() {
            var filename = $('#hit').attr('fileName');
            var img_data = $('#hit').attr('src');
            if (img_data == "") {
                alert('null');
            }
            var str = {
                "baseimg": img_data
            };
            $.ajax('/song/fileUploadApi/imgbase64', {
                dataType: 'json',//服务器返回json格式数据
                type: 'post',//HTTP请求类型
                data: str,
                timeout: 10000,//超时时间设置为10秒;
                success: function (data) {
                    $("#showImg").attr("src", "/song/" + data.imgUrl);
                    $("#sImg").val(data.imgUrl);
                    $("#yuan-box").show();
                    $("#pic-boox").hide();
                }
            })
        }

        // 获取文件拓展名
        function getFileExt(str) {
            var d = /\.[^\.]+$/.exec(str);
            return d;
        }

        //取消上传
        $("#upload2").click(function () {
            $("#pic-boox").hide();
            $("#yuan-box").show();
        })
        /** 上传图片结束 */

    }



         //这里可以优化一下


});


var baseUrl = "http://www.muzhitui.cn/song/";
function addTop(data) {
    // var data = jQuery.parseJSON(data);
    var linkUrl = data.linkUrl;
    if (linkUrl.indexOf("http://") <= -1) {
        linkUrl = "http://" + linkUrl;
    }
    var advTop = "<span style='color:#8C8C8C'>" + data.topDate + "</span>&nbsp;&nbsp;&nbsp;&nbsp;<a class='songAdv' href='" + linkUrl + "'>" + data.publicNo + "</a>";
    advTop += '<br/><img width="100%" src="' + baseUrl + data.img + '"/>';
    $(".bg").after(advTop);
    $("body").show();
    $(".over").show();
    $("#openadvert").hide();
    var h = $(".bg").offset().top - 100;
    $(window).scrollTop(h);
    var xx = $("#hidf").val();
    $(".bg").css("background", xx);
    $(".editorgao").remove();
    $("body *:not(span)").removeClass("bg");
    $(".show").hide();
    return advTop;
}


function addBottom(data) {
    //  var data = jQuery.parseJSON(data);
    var advBop = '<div style="text-align:center;"><div>' + data.title1 + '</div><div><img width="100%" src="' + baseUrl + data.img + '"/></div><div>' + data.title2 + '</div></div>';
    $(".bg").after(advBop);
    $("body").show();
    $(".over").show();
    $("#openadvert").hide();
    var h = $(".bg").offset().top - 100;
    $(window).scrollTop(h);
    var xx = $("#hidf").val();
    $(".bg").css("background", xx);
    $(".editorgao").remove();
    $("body *:not(span)").removeClass("bg");
    $(".show").hide();
    return advBop;
}
var arrayFontAnimation = new Array("expand", "fadeIn", "narrow", "shake-horizontal", "shake-vertical");
function addAd(data) {
    //var data = jQuery.parseJSON(data);
    var adurl = data.adurl;
    if(adurl.indexOf("http")== -1) {
       adurl = "http://"+adurl;
    }
    $("#advertimg").attr("src", data.adimage);
    var adv = '<a class="umeixAdv songAdv" href="' + adurl + '" style="text-align:center;display: inline-block;width:100%;">';
    if (null != data.adimage && data.adimage != "") {
        adv += '<div class="divChoiceImg"> <img width="100%" src="' + baseUrl + data.adimage + '">' + '<div class="divText"> <label class="lbText" style="';
        adv += 'color:' + data.adcolor + ";";
        adv += 'font-family:' + data.font + ";";
        adv += 'font-size:' + (15 + (data.size - 0.5) * 15) + "px;";
        if (data.animate != -1) {
            adv += '-webkit-animation-name:' + arrayFontAnimation[data.animate];
        }
        adv += '">' + data.adtext + '</label></div> </img></div></a>';
    }
    $(".bg").after(adv);
    $("body").show();
    $(".over").show();
    $("#openadvert").hide();
    var h = $(".bg").offset().top - 100;
    $(window).scrollTop(h);
    var xx = $("#hidf").val();
    $(".bg").css("background", xx);
    $(".editorgao").remove();
    $("body *:not(span)").removeClass("bg");
    $(".show").hide();
    return adv;
}

function addFontAlign(index) {
    $(".bg").removeClass("text-cen");
    $(".bg").removeClass("text-rt");
    $(".bg").removeClass("text-lt");
    $(".textarea").removeClass("text-cen");
    $(".textarea").removeClass("text-rt");
    $(".textarea").removeClass("text-lt");
    if (index == 0) {//居左对齐
        $(".bg").addClass("text-lt");
        $(".textarea").removeClass("text-lt");
    } else if (index == 1) {//居中对齐
        $(".bg").addClass("text-cen");
        $(".textarea").removeClass("text-cen");
    } else if (index == 2) {//居右对齐
        $(".bg").addClass("text-rt");
        $(".textarea").removeClass("text-rt");
    }
}

function setFontColor(myColor) {
    $(".bg").css({"color": myColor});
}


function setFontAnimation(index) {
    $(".bg").removeClass("fadein");
    $(".bg").removeClass("narrow");
    $(".bg").removeClass("shake-horizontal");
    $(".bg").removeClass("shake-vertical");
    $(".bg").removeClass("expand");
    if (index == 0) {
        $(".bg").addClass("expand");
    } else if (index == 1) {
        $(".bg").addClass("fadein");
    } else if (index == 2) {
        $(".bg").addClass("narrow");
    } else if (index == 3) {
        $(".bg").addClass("shake-horizontal");
    } else if (index == 4) {
        $(".bg").addClass("shake-vertical");
    }
}

var fontSize = 20; //用系统字体大小？

function setFontSize(size) {
    $(".bg").css({
        "font-size": (fontSize + (size - 0.5) * fontSize ) + "px",
        "line-height": (fontSize + (size - 0.5) * fontSize ) + "px"
    });
}
function setFontFamily(fontFamily) {
    $(".bg").css({"font-family": fontFamily});
}

function fontCancle() {
    addFontAlign(1000);
    setFontFamily("lala");
    setFontColor("#000");
    setFontSize(0.5);
    setFontAnimation(10);
    $(".show").hide();
    $(".editor").find("li").eq(0).hide();
    $(".editorgao").remove();
    var xx = $("#hidf").val();
    $(".bg").css("background", xx);
    $("body *:not(span)").removeClass("bg");
    event.stopImmediatePropagation();
    //样式取消
    $(".typelist").hide();
    $(".show").show();
    $("body").show();
    var h = $(".bg").offset().top - 100;
    $(window).scrollTop(h);
    for (var k = 0; k < 10; k++) {
        $(".textarea").removeClass("color" + k);
    }
    $(".textarea").removeClass("middlesize");
    $(".textarea").removeClass("bigsize");
    $(".textarea").removeClass("smallsize");
    $(".textarea").removeClass("text-lt");
    $(".textarea").removeClass("text-cen");
    $(".textarea").removeClass("text-rt");
    $("#textarea").removeClass("wori");


    event.stopImmediatePropagation();
}

function fontConfirm(textContent) {
    var bg = $("#hidf").val();
    $(".bg").css("background", bg);

    $(".bg").html(textContent);

    $("body *:not(span)").removeClass("bg");
    $(".editorgao").remove();
    $(".show").hide();


    $(".editor").find("li").eq(0).hide();

    event.stopImmediatePropagation();
}

function cancelYinyue() {
    $(".audio").remove();
    $("body").show();
    $(".yingyuelist").hide();
    $(".over").show();
    $(".zhanting").remove();
    event.stopImmediatePropagation();
}


function getHtmlContent() {
    $(".show").remove();
  /*  var pagehtml = $("body").html();
    var pagehead = $("head").html();
    var content = "<html><head>" + pagehead + "</head><body>" + pagehtml + "</body></html>"*/
 /*   var content = document.getElementsByTagName('html')[0].innerHTML;
    return content;*/
    return document.getElementsByTagName('html')[0].innerHTML;
}
//完成
var m = 0;
function save(data) {
    $("#sureBox").remove(); // 移除确认框
    // 如果保存为草稿，就直接保存，不去进行标题和缩略图的修改了。
    if (data == 0) {
        m = 1;
    }
    if (m == 0) {
        $("body").hide();
        $(".over").show();
        $(".title").show();
        var xx = $("#hidf").val();
        $(".bg").css("background", xx);
        $(".editorgao").remove();
        $(".show").remove();
    } else if (m == 1) {
        $(".loading-mc").show();
        $(".show").remove();
        // 如果存在音乐，就显示播放暂停图标
        if (document.getElementById("music")) {
            $(".zhanting").show();
        }
        var title = $("#title").val();
        var pagehtml = $("body").html();
        var pagehead = $("head").html();
        $(".title").hide();
        $(".over").hide();
        var str = {
            "pagehtml": "<html><head>" + pagehead + "</head><body>" + pagehtml + "</body></html>",
            "title": title,
            "website": website,
            "image": $("#sImg").val(),
            "state": data,
            "id": id
        };
        $.ajax(baseUrl + 'appPageApi/doSave', {
            dataType: 'json',//服务器返回json格式数据
            type: 'post',//HTTP请求类型
            data: str,
            //timeout:10000,//超时时间设置为10秒；
            success: function (data) {
                $(".title").remove();
                $(".over").remove();
                if (data.flag) {
                    window.location.href = baseUrl + 'appPageApi/pageDetail?id=' + data.pageid;
                } else {
                    alert("请检查网络");
                }
            }
        });
    }
    m++;
    if (m >= 2) {
        m = 0;
    }
}


     function fetchRequestParmValue(url, paras) {
            var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
            var paraObj = {}
            for (i = 0; j = paraString[i]; i++) {
                paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
            }
            var returnValue = paraObj[paras.toLowerCase()];
            if (typeof (returnValue) == "undefined") {
                return "";
            } else {
                return returnValue;
            }
     }

$(".add-textr").click(function (ev) {
    return false;
});




   