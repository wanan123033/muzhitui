package com.nevermore.muzhitui.module.bean;

/**
 * Created by hehe on 2016/6/21.
 */
public class SecondPage {


    /**
     * status : 1
     * page : {"id":null,"pagecontent":"<!DOCTYPE html>\n<html>\n    <head>\n        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0\" />\n<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n<meta name=\"format-detection\" content=\"telephone=no\">\n\n\n        <script type=\"text/javascript\">\n            window.logs = {\n                pagetime: {}\n            };\n            window.logs.pagetime['html_begin'] = (+new Date());\n        <\/script>\n        \n        <link rel=\"dns-prefetch\" href=\"//res.wx.qq.com\">\n<link rel=\"dns-prefetch\" href=\"//mmbiz.qpic.cn\">\n<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/icon/common/favicon22c41b.ico\">\n<script type=\"text/javascript\">\n    String.prototype.html = function(encode) {\n        var replace =[\"&#39;\", \"'\", \"&quot;\", '\"', \"&nbsp;\", \" \", \"&gt;\", \">\", \"&lt;\", \"<\", \"&amp;\", \"&\", \"&yen;\", \"¥\"];\n        if (encode) {\n            replace.reverse();\n        }\n        for (var i=0,str=this;i< replace.length;i+= 2) {\n             str=str.replace(new RegExp(replace[i],'g'),replace[i+1]);\n        }\n        return str;\n    };\n\n    window.isInWeixinApp = function() {\n        return /MicroMessenger/.test(navigator.userAgent);\n    };\n\n    window.getQueryFromURL = function(url) {\n        url = url || 'http://qq.com/s?a=b#rd'; \n        var query = url.split('?')[1].split('#')[0].split('&'),\n            params = {};\n        for (var i=0; i<query.length; i++) {\n            var arg = query[i].split('=');\n            params[arg[0]] = arg[1];\n        }\n        if (params['pass_ticket']) {\n        \tparams['pass_ticket'] = encodeURIComponent(params['pass_ticket'].html(false).html(false).replace(/\\s/g,\"+\"));\n        }\n        return params;\n    };\n\n    (function() {\n\t    var params = getQueryFromURL(location.href);\n        window.uin = params['uin'] || '';\n        window.key = params['key'] || '';\n        window.wxtoken = params['wxtoken'] || '';\n        window.pass_ticket = params['pass_ticket'] || '';\n    })();\n\n<\/script>\n\n        <title><\/title>\n        \n<style>.icon_msg{width:100px;height:100px;vertical-align:middle;display:inline-block}.icon_msg.warn{background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAMAAABHPGVmAAAA51BMVEUAAAD4Y2H4Y2H6Y2L/aGj4YmD/jo74YmD3YmD4Y2H5Y2D/aGj5ZGP3Y2H3Y2D4YmH4YmH4YmH5Y2H4ZGH4Z2L4YmD4Y2D4Y2D3YmH3Y2H4Y2H5Y2H5Y2H5Y2H7ZWL/amr3YmD4Y2D4Y2D4Y2D4YmD3Y2H3ZGL4Y2H3ZGH6ZWL/ZGT3Y2H3YmL3Y2D4YmH4Y2H5Y2P3YmL/amr/cXH3YmD////93t75kY/+9/f95eX7vLz91tX8xcT7s7L6qKb5jYz4iIb4eXf+8PD+/Pz+7ez96ur8zc36np35mZj4goD3cG73amj+8fEx3c3LAAAANHRSTlMA+/U0EvgE38SLWA0s7ejSz7tfSSPx3cqnpJV7d1M4DOza1bCbhYBwQjAX62XHv7pQQRgJr3XL4gAAAwtJREFUaN7t2udSIkEUhuHTE4AhZxQFiQrGVT+CGMCc9v6vZ92uHUFJp2d6fmyVzw281XSgKZp+/L9C8crWZiQVTgj7IJyKbOxGzR29AXP7xMAcUXJyMdLDLNewlMhk/Q/I2m5gDWOz4y/RLoCj9Mt7wjHA1YiSJ9EkVBzHSVk+A0XCUV0CWQPqiiYpOG/BE3tP4aNKw6smd3fGk/AuvU8c1QL8KOYZjZwBf5Lx9eMw4Fdy3VjyB/CvuHpe9ovQIb1qjYUi0KNJy7Why/Jd2RHQxTaXNKwk9CkuOS1b0MmhRUwBncTCPVmCXsc0LwvdovNbpA6O8UOv13sYg6MxF4mC5aL31wVY5u4wabDcycgdWEr0VQ48jzLyCJ4OfbGBAD4ubNKsmA2evoz0wWPseFq/lzJyCaYszcgEFMnQVEyA6UlGnsAkYoprS7qSkStw5aYRJ7CIo7gTpZGMjMBV8jAluJaRa3CJz0VsIrAITLXDUbqRkRuon/e7AUZ21Q4u6VlGnsG24UYiYHuRkRewRdxICmwDGRmALeVGwmAby8gYbGE3UgPbREYmYKu5ERtsrzLyCjbbjQiw3crILdiEG0kEGEl4mPg3GXkDW9jDEn6XkXewpTxsRiEjAlDejJvgu/9o3ENSuxVtgW/4ERmCb8uNVMD3W95S+SpuJA6+C3mB5Iu7kZABtsloNJqAzQgp3+3UndCnbQRlexoxERRzGgklEIxaiKbK4Br0h/0BuMo0owoW5a9fk2bVwaH6I6hBkurJMpSRofLakiwjgJEULPrKCWBO2vTNma19dRkWfVeGbg7NsRLQK2nRvEPoFaVFjqBThhbqCuhj5GmxPeiTpWWa0KVFS1lh6JE+p+W6BShT/9/h1IZ/hTitVhHwy6jSOofCbyNH61Vs+HFQJY7TArwr5omnG4ZXkX3isprwRLRDpGBPQF2yQ2q6R1AkWhYpO0xARckkL6yyDa56lrw6cwxwpKMh8sHaqmMNeyNHvlXLiZXvVmLaXuBkjAWBtJOLBfWWqPbvLVGMfgThDwtsVzmQb2+9AAAAAElFTkSuQmCC)}@media only screen and (-webkit-min-device-pixel-ratio:2){.icon_msg.warn{background-image:url(data:image/png;base64,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);-webkit-background-size:100px auto;background-size:100px auto}}.icon_msg.warn_gray{background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAAC6CAMAAAAu0KfDAAACr1BMVEUAAAD////////////////MzMzV1dXb29vf39/j4+PMzMzR0dHV1dXMzMzS0tLV1dXJycnMzMzOzs7KysrMzMzOzs7R0dHKysrMzMzOzs7R0dHLy8vNzc3JycnOzs7KysrLy8vMzMzOzs7MzMzNzc3Ly8vMzMzJycnKysrLy8vLy8vNzc3Ly8vLy8vMzMzKysrLy8vJycnKysrLy8vMzMzKysrLy8vLy8vJycnKysrKysrLy8vJycnKysrKysrLy8vLy8vJycnKysrKysrLy8vJycnKysrLy8vKysrLy8vLy8vJycnKysrJycnKysrJycnKysrKysrLy8vJycnKysrKysrKysrJycnKysrKysrLy8vJycnKysrJycnJycnKysrKysrJycnKysrKysrKysrLy8vJycnKysrKysrKysrJycnKysrJycnJycnKysrKysrKysrJycnKysrKysrKysrKysrKysrJycnKysrKysrKysrKysrKysrJycnKysrJycnKysrKysrJycnJycnKysrJycnKysrJycnKysrKysrKysrJycnJycnKysrKysrKysrJycnJycnKysrKysrJycnKysrKysrJycnJycnKysrJycnJycnKysrKysrKysrJycnJycnKysrKysrJycnJycnJycnKysrKysrJycnJycnKysrKysrJycnJycnKysrKysrJycnKysrLy8vMzMzNzc3Ozs7Pz8/Q0NDR0dHS0tLT09PU1NTV1dXW1tbX19fY2NjZ2dna2trb29vc3Nzd3d3e3t7f39/g4ODh4eHi4uLj4+Pk5OTl5eXm5ubn5+fo6Ojp6enq6urr6+vs7Ozt7e3u7u7v7+/w8PDx8fHy8vLz8/P09PT19fX29vb39/f4+Pj5+fn6+vr8/Pz+/v7///8+hfkLAAAAsHRSTlMAAQIDBAUGBwgJCgsMDxESExQVGBkaHB0eHyEiJCYqKywtLzIzNjc5OjtAQkRFRkhKTE1PUFJTVFVWV1haW1xdXl9gYWNkZWZpa2xtb3J0d3h5ent8fX6AgYKEhYeJioyPk5SVlpeYmZqbnKChoqWnqausra6xsrW2uLq7vL2/w8TFx8jKzM/R0tPU1dbX2Nna29zd3uHi4+Tm6Onq6+zt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)}@media only screen and (-webkit-min-device-pixel-ratio:2){.icon_msg.warn_gray{background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAAC6CAMAAAAu0KfDAAACr1BMVEUAAAD////////////////MzMzV1dXb29vf39/j4+PMzMzR0dHV1dXMzMzS0tLV1dXJycnMzMzOzs7KysrMzMzOzs7R0dHKysrMzMzOzs7R0dHLy8vNzc3JycnOzs7KysrLy8vMzMzOzs7MzMzNzc3Ly8vMzMzJycnKysrLy8vLy8vNzc3Ly8vLy8vMzMzKysrLy8vJycnKysrLy8vMzMzKysrLy8vLy8vJycnKysrKysrLy8vJycnKysrKysrLy8vLy8vJycnKysrKysrLy8vJycnKysrLy8vKysrLy8vLy8vJycnKysrJycnKysrJycnKysrKysrLy8vJycnKysrKysrKysrJycnKysrKysrLy8vJycnKysrJycnJycnKysrKysrJycnKysrKysrKysrLy8vJycnKysrKysrKysrJycnKysrJycnJycnKysrKysrKysrJycnKysrKysrKysrKysrKysrJycnKysrKysrKysrKysrKysrJycnKysrJycnKysrKysrJycnJycnKysrJycnKysrJycnKysrKysrKysrJycnJycnKysrKysrKysrJycnJycnKysrKysrJycnKysrKysrJycnJycnKysrJycnJycnKysrKysrKysrJycnJycnKysrKysrJycnJycnJycnKysrKysrJycnJycnKysrKysrJycnJycnKysrKysrJycnKysrLy8vMzMzNzc3Ozs7Pz8/Q0NDR0dHS0tLT09PU1NTV1dXW1tbX19fY2NjZ2dna2trb29vc3Nzd3d3e3t7f39/g4ODh4eHi4uLj4+Pk5OTl5eXm5ubn5+fo6Ojp6enq6urr6+vs7Ozt7e3u7u7v7+/w8PDx8fHy8vLz8/P09PT19fX29vb39/f4+Pj5+fn6+vr8/Pz+/v7///8+hfkLAAAAsHRSTlMAAQIDBAUGBwgJCgsMDxESExQVGBkaHB0eHyEiJCYqKywtLzIzNjc5OjtAQkRFRkhKTE1PUFJTVFVWV1haW1xdXl9gYWNkZWZpa2xtb3J0d3h5ent8fX6AgYKEhYeJioyPk5SVlpeYmZqbnKChoqWnqausra6xsrW2uLq7vL2/w8TFx8jKzM/R0tPU1dbX2Nna29zd3uHi4+Tm6Onq6+zt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);-webkit-background-size:100px auto;background-size:100px auto}}body{background-color:#fff}a{color:#607fa6;text-decoration:none}a:visited{color:#607fa6}.page_msg{padding:75px 15px 0;text-align:center}.page_msg.primary .text_area{color:#8c8c8c}.page_msg.primary .text_area .tips{color:#3e3e3e}.icon_area{margin-bottom:19px}.text_area{margin-bottom:25px}.text_area .title{margin-bottom:12px}.text_area .tips{color:#8c8c8c}.extra_area{margin-bottom:20px;font-size:0}.extra_area a{position:relative;margin:0 .75em;font-size:14px}.extra_area a:before{content:\" \";position:absolute;left:0;top:0;width:1px;bottom:0;border-left:1px solid #dbdbdb;-webkit-transform-origin:0 0;transform-origin:0 0;-webkit-transform:scaleX(0.5);transform:scaleX(0.5);left:-0.75em}.extra_area a:first-child:before{display:none}@media screen and (min-height:416px){.extra_area{position:fixed;left:0;bottom:0;width:100%}}<\/style>\n\n    <\/head>\n    <body id=\"activity-detail\" class=\"zh_CN \" ontouchstart=\"\">\n        \n<div class=\"page_msg\">\n    <div class=\"icon_area\"><i class=\"icon_msg warn\"><\/i><\/div>\n    <div class=\"text_area\">\n                    <div class=\"global_error_msg warn\">\n            参数错误            <\/div>\n            <\/div>\n\n        <div class=\"extra_area\">\n        <p class=\"tips\">\n            <a href=\"http://mp.weixin.qq.com/mp/opshowpage?action=main#wechat_redirect\">微信公众平台运营中心<\/a>\n        <\/p>\n    <\/div>\n    <\/div>\n\n        \n        <script>\n    var __DEBUGINFO = {\n        debug_js : \"http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/js/biz_wap/debug/console2ca724.js\",\n        safe_js : \"http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/js/biz_wap/safe/moonsafe2e21ab.js\",\n        res_list: []\n    };\n<\/script>\n        \n  <script type=\"text/javascript\">\n        var biz = \"\"||\"\";\n        var sn = \"\" || \"\";\n        var mid = \"\" || \"\"|| \"\";\n        var idx = \"\" || \"\" || \"\" ;\n      \n      \n      var is_rumor = \"\"*1;\n      var norumor = \"\"*1;\n      \n      if (!!is_rumor&&!norumor){\n          \n          if (!document.referrer || document.referrer.indexOf(\"mp.weixin.qq.com/mp/rumor\") == -1){\n            location.href = location.protocol + \"//mp.weixin.qq.com/mp/rumor?action=info&__biz=\" + biz + \"&mid=\" + mid + \"&idx=\" + idx + \"&sn=\" + sn + \"#wechat_redirect\";\n            \n          }\n      }else{\n        function onBridgeReady() {\n            WeixinJSBridge.call(\"hideToolbar\");  \n            WeixinJSBridge.call(\"hideOptionMenu\");\n        }\n        if (typeof WeixinJSBridge == \"undefined\"){\n            if( document.addEventListener ){\n                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);\n            }else if (document.attachEvent){\n                document.attachEvent('WeixinJSBridgeReady', onBridgeReady); \n                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);\n            }\n        }else{\n            onBridgeReady();\n        }\n      }\n  <\/script>\n\n    <\/body>\n<\/html>\n\n","website":"http://mp.weixin.qq.com/s?src=3","image":null}
     */

    private String status;
    /**
     * id : null
     * pagecontent : <!DOCTYPE html>
     <html>
     <head>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
     <meta http-equiv="X-UA-Compatible" content="IE=edge">
     <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
     <meta name="apple-mobile-web-app-capable" content="yes">
     <meta name="apple-mobile-web-app-status-bar-style" content="black">
     <meta name="format-detection" content="telephone=no">


     <script type="text/javascript">
     window.logs = {
     pagetime: {}
     };
     window.logs.pagetime['html_begin'] = (+new Date());
     </script>

     <link rel="dns-prefetch" href="//res.wx.qq.com">
     <link rel="dns-prefetch" href="//mmbiz.qpic.cn">
     <link rel="shortcut icon" type="image/x-icon" href="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/icon/common/favicon22c41b.ico">
     <script type="text/javascript">
     String.prototype.html = function(encode) {
     var replace =["&#39;", "'", "&quot;", '"', "&nbsp;", " ", "&gt;", ">", "&lt;", "<", "&amp;", "&", "&yen;", "¥"];
     if (encode) {
     replace.reverse();
     }
     for (var i=0,str=this;i< replace.length;i+= 2) {
     str=str.replace(new RegExp(replace[i],'g'),replace[i+1]);
     }
     return str;
     };

     window.isInWeixinApp = function() {
     return /MicroMessenger/.test(navigator.userAgent);
     };

     window.getQueryFromURL = function(url) {
     url = url || 'http://qq.com/s?a=b#rd';
     var query = url.split('?')[1].split('#')[0].split('&'),
     params = {};
     for (var i=0; i<query.length; i++) {
     var arg = query[i].split('=');
     params[arg[0]] = arg[1];
     }
     if (params['pass_ticket']) {
     params['pass_ticket'] = encodeURIComponent(params['pass_ticket'].html(false).html(false).replace(/\s/g,"+"));
     }
     return params;
     };

     (function() {
     var params = getQueryFromURL(location.href);
     window.uin = params['uin'] || '';
     window.key = params['key'] || '';
     window.wxtoken = params['wxtoken'] || '';
     window.pass_ticket = params['pass_ticket'] || '';
     })();

     </script>

     <title></title>

     <style>.icon_msg{width:100px;height:100px;vertical-align:middle;display:inline-block}.icon_msg.warn{background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAMAAABHPGVmAAAA51BMVEUAAAD4Y2H4Y2H6Y2L/aGj4YmD/jo74YmD3YmD4Y2H5Y2D/aGj5ZGP3Y2H3Y2D4YmH4YmH4YmH5Y2H4ZGH4Z2L4YmD4Y2D4Y2D3YmH3Y2H4Y2H5Y2H5Y2H5Y2H7ZWL/amr3YmD4Y2D4Y2D4Y2D4YmD3Y2H3ZGL4Y2H3ZGH6ZWL/ZGT3Y2H3YmL3Y2D4YmH4Y2H5Y2P3YmL/amr/cXH3YmD////93t75kY/+9/f95eX7vLz91tX8xcT7s7L6qKb5jYz4iIb4eXf+8PD+/Pz+7ez96ur8zc36np35mZj4goD3cG73amj+8fEx3c3LAAAANHRSTlMA+/U0EvgE38SLWA0s7ejSz7tfSSPx3cqnpJV7d1M4DOza1bCbhYBwQjAX62XHv7pQQRgJr3XL4gAAAwtJREFUaN7t2udSIkEUhuHTE4AhZxQFiQrGVT+CGMCc9v6vZ92uHUFJp2d6fmyVzw281XSgKZp+/L9C8crWZiQVTgj7IJyKbOxGzR29AXP7xMAcUXJyMdLDLNewlMhk/Q/I2m5gDWOz4y/RLoCj9Mt7wjHA1YiSJ9EkVBzHSVk+A0XCUV0CWQPqiiYpOG/BE3tP4aNKw6smd3fGk/AuvU8c1QL8KOYZjZwBf5Lx9eMw4Fdy3VjyB/CvuHpe9ovQIb1qjYUi0KNJy7Why/Jd2RHQxTaXNKwk9CkuOS1b0MmhRUwBncTCPVmCXsc0LwvdovNbpA6O8UOv13sYg6MxF4mC5aL31wVY5u4wabDcycgdWEr0VQ48jzLyCJ4OfbGBAD4ubNKsmA2evoz0wWPseFq/lzJyCaYszcgEFMnQVEyA6UlGnsAkYoprS7qSkStw5aYRJ7CIo7gTpZGMjMBV8jAluJaRa3CJz0VsIrAITLXDUbqRkRuon/e7AUZ21Q4u6VlGnsG24UYiYHuRkRewRdxICmwDGRmALeVGwmAby8gYbGE3UgPbREYmYKu5ERtsrzLyCjbbjQiw3crILdiEG0kEGEl4mPg3GXkDW9jDEn6XkXewpTxsRiEjAlDejJvgu/9o3ENSuxVtgW/4ERmCb8uNVMD3W95S+SpuJA6+C3mB5Iu7kZABtsloNJqAzQgp3+3UndCnbQRlexoxERRzGgklEIxaiKbK4Br0h/0BuMo0owoW5a9fk2bVwaH6I6hBkurJMpSRofLakiwjgJEULPrKCWBO2vTNma19dRkWfVeGbg7NsRLQK2nRvEPoFaVFjqBThhbqCuhj5GmxPeiTpWWa0KVFS1lh6JE+p+W6BShT/9/h1IZ/hTitVhHwy6jSOofCbyNH61Vs+HFQJY7TArwr5omnG4ZXkX3isprwRLRDpGBPQF2yQ2q6R1AkWhYpO0xARckkL6yyDa56lrw6cwxwpKMh8sHaqmMNeyNHvlXLiZXvVmLaXuBkjAWBtJOLBfWWqPbvLVGMfgThDwtsVzmQb2+9AAAAAElFTkSuQmCC)}@media only screen and (-webkit-min-device-pixel-ratio:2){.icon_msg.warn{background-image:url(data:image/png;base64,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);-webkit-background-size:100px auto;background-size:100px auto}}.icon_msg.warn_gray{background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAAC6CAMAAAAu0KfDAAACr1BMVEUAAAD////////////////MzMzV1dXb29vf39/j4+PMzMzR0dHV1dXMzMzS0tLV1dXJycnMzMzOzs7KysrMzMzOzs7R0dHKysrMzMzOzs7R0dHLy8vNzc3JycnOzs7KysrLy8vMzMzOzs7MzMzNzc3Ly8vMzMzJycnKysrLy8vLy8vNzc3Ly8vLy8vMzMzKysrLy8vJycnKysrLy8vMzMzKysrLy8vLy8vJycnKysrKysrLy8vJycnKysrKysrLy8vLy8vJycnKysrKysrLy8vJycnKysrLy8vKysrLy8vLy8vJycnKysrJycnKysrJycnKysrKysrLy8vJycnKysrKysrKysrJycnKysrKysrLy8vJycnKysrJycnJycnKysrKysrJycnKysrKysrKysrLy8vJycnKysrKysrKysrJycnKysrJycnJycnKysrKysrKysrJycnKysrKysrKysrKysrKysrJycnKysrKysrKysrKysrKysrJycnKysrJycnKysrKysrJycnJycnKysrJycnKysrJycnKysrKysrKysrJycnJycnKysrKysrKysrJycnJycnKysrKysrJycnKysrKysrJycnJycnKysrJycnJycnKysrKysrKysrJycnJycnKysrKysrJycnJycnJycnKysrKysrJycnJycnKysrKysrJycnJycnKysrKysrJycnKysrLy8vMzMzNzc3Ozs7Pz8/Q0NDR0dHS0tLT09PU1NTV1dXW1tbX19fY2NjZ2dna2trb29vc3Nzd3d3e3t7f39/g4ODh4eHi4uLj4+Pk5OTl5eXm5ubn5+fo6Ojp6enq6urr6+vs7Ozt7e3u7u7v7+/w8PDx8fHy8vLz8/P09PT19fX29vb39/f4+Pj5+fn6+vr8/Pz+/v7///8+hfkLAAAAsHRSTlMAAQIDBAUGBwgJCgsMDxESExQVGBkaHB0eHyEiJCYqKywtLzIzNjc5OjtAQkRFRkhKTE1PUFJTVFVWV1haW1xdXl9gYWNkZWZpa2xtb3J0d3h5ent8fX6AgYKEhYeJioyPk5SVlpeYmZqbnKChoqWnqausra6xsrW2uLq7vL2/w8TFx8jKzM/R0tPU1dbX2Nna29zd3uHi4+Tm6Onq6+zt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)}@media only screen and (-webkit-min-device-pixel-ratio:2){.icon_msg.warn_gray{background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAAC6CAMAAAAu0KfDAAACr1BMVEUAAAD////////////////MzMzV1dXb29vf39/j4+PMzMzR0dHV1dXMzMzS0tLV1dXJycnMzMzOzs7KysrMzMzOzs7R0dHKysrMzMzOzs7R0dHLy8vNzc3JycnOzs7KysrLy8vMzMzOzs7MzMzNzc3Ly8vMzMzJycnKysrLy8vLy8vNzc3Ly8vLy8vMzMzKysrLy8vJycnKysrLy8vMzMzKysrLy8vLy8vJycnKysrKysrLy8vJycnKysrKysrLy8vLy8vJycnKysrKysrLy8vJycnKysrLy8vKysrLy8vLy8vJycnKysrJycnKysrJycnKysrKysrLy8vJycnKysrKysrKysrJycnKysrKysrLy8vJycnKysrJycnJycnKysrKysrJycnKysrKysrKysrLy8vJycnKysrKysrKysrJycnKysrJycnJycnKysrKysrKysrJycnKysrKysrKysrKysrKysrJycnKysrKysrKysrKysrKysrJycnKysrJycnKysrKysrJycnJycnKysrJycnKysrJycnKysrKysrKysrJycnJycnKysrKysrKysrJycnJycnKysrKysrJycnKysrKysrJycnJycnKysrJycnJycnKysrKysrKysrJycnJycnKysrKysrJycnJycnJycnKysrKysrJycnJycnKysrKysrJycnJycnKysrKysrJycnKysrLy8vMzMzNzc3Ozs7Pz8/Q0NDR0dHS0tLT09PU1NTV1dXW1tbX19fY2NjZ2dna2trb29vc3Nzd3d3e3t7f39/g4ODh4eHi4uLj4+Pk5OTl5eXm5ubn5+fo6Ojp6enq6urr6+vs7Ozt7e3u7u7v7+/w8PDx8fHy8vLz8/P09PT19fX29vb39/f4+Pj5+fn6+vr8/Pz+/v7///8+hfkLAAAAsHRSTlMAAQIDBAUGBwgJCgsMDxESExQVGBkaHB0eHyEiJCYqKywtLzIzNjc5OjtAQkRFRkhKTE1PUFJTVFVWV1haW1xdXl9gYWNkZWZpa2xtb3J0d3h5ent8fX6AgYKEhYeJioyPk5SVlpeYmZqbnKChoqWnqausra6xsrW2uLq7vL2/w8TFx8jKzM/R0tPU1dbX2Nna29zd3uHi4+Tm6Onq6+zt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);-webkit-background-size:100px auto;background-size:100px auto}}body{background-color:#fff}a{color:#607fa6;text-decoration:none}a:visited{color:#607fa6}.page_msg{padding:75px 15px 0;text-align:center}.page_msg.primary .text_area{color:#8c8c8c}.page_msg.primary .text_area .tips{color:#3e3e3e}.icon_area{margin-bottom:19px}.text_area{margin-bottom:25px}.text_area .title{margin-bottom:12px}.text_area .tips{color:#8c8c8c}.extra_area{margin-bottom:20px;font-size:0}.extra_area a{position:relative;margin:0 .75em;font-size:14px}.extra_area a:before{content:" ";position:absolute;left:0;top:0;width:1px;bottom:0;border-left:1px solid #dbdbdb;-webkit-transform-origin:0 0;transform-origin:0 0;-webkit-transform:scaleX(0.5);transform:scaleX(0.5);left:-0.75em}.extra_area a:first-child:before{display:none}@media screen and (min-height:416px){.extra_area{position:fixed;left:0;bottom:0;width:100%}}</style>

     </head>
     <body id="activity-detail" class="zh_CN " ontouchstart="">

     <div class="page_msg">
     <div class="icon_area"><i class="icon_msg warn"></i></div>
     <div class="text_area">
     <div class="global_error_msg warn">
     参数错误            </div>
     </div>

     <div class="extra_area">
     <p class="tips">
     <a href="http://mp.weixin.qq.com/mp/opshowpage?action=main#wechat_redirect">微信公众平台运营中心</a>
     </p>
     </div>
     </div>


     <script>
     var __DEBUGINFO = {
     debug_js : "http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/js/biz_wap/debug/console2ca724.js",
     safe_js : "http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/js/biz_wap/safe/moonsafe2e21ab.js",
     res_list: []
     };
     </script>

     <script type="text/javascript">
     var biz = ""||"";
     var sn = "" || "";
     var mid = "" || ""|| "";
     var idx = "" || "" || "" ;


     var is_rumor = ""*1;
     var norumor = ""*1;

     if (!!is_rumor&&!norumor){

     if (!document.referrer || document.referrer.indexOf("mp.weixin.qq.com/mp/rumor") == -1){
     location.href = location.protocol + "//mp.weixin.qq.com/mp/rumor?action=info&__biz=" + biz + "&mid=" + mid + "&idx=" + idx + "&sn=" + sn + "#wechat_redirect";

     }
     }else{
     function onBridgeReady() {
     WeixinJSBridge.call("hideToolbar");
     WeixinJSBridge.call("hideOptionMenu");
     }
     if (typeof WeixinJSBridge == "undefined"){
     if( document.addEventListener ){
     document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
     }else if (document.attachEvent){
     document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
     document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
     }
     }else{
     onBridgeReady();
     }
     }
     </script>

     </body>
     </html>


     * website : http://mp.weixin.qq.com/s?src=3
     * image : null
     */

    private PageBean page;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public static class PageBean {
        private String id;
        private String pagecontent;
        private String website;
        private String image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPagecontent() {
            return pagecontent;
        }

        public void setPagecontent(String pagecontent) {

//            StringBuilder sb = new StringBuilder(pagecontent);
//            sb.append("<script type=\"text/javascript\">\n" +
//                    "\tfunction videoWidth(){\n" +
//                    "\t\tvar iframe = document.getElementsByTagName(\"iframe\");\n" +
//                    "\t\tiframe[0].style.cssText += \";width:375px!important\";\n" +
//                    "\t\tiframe[0].style.cssText += \";height:278px!important\";\n" +
//                    "\t}\n" +
//                    "\tvideoWidth();\n" +
//                    "</script>");
            this.pagecontent = pagecontent;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public String toString() {
            return "PageBean{" +
                    "id='" + id + '\'' +
                    ", pagecontent='" + pagecontent + '\'' +
                    ", website='" + website + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SecondPage{" +
                "status='" + status + '\'' +
                ", page=" + page +
                '}';
    }
}
