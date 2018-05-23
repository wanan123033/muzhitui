/**
 * Created by hehe on 2016/6/14.
 */

function connectWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
        callback(WebViewJavascriptBridge)
    } else {
        document.addEventListener(
            'WebViewJavascriptBridgeReady'
            , function () {
                callback(WebViewJavascriptBridge)
            },
            false
        );
    }
}

connectWebViewJavascriptBridge(function (bridge) {

    bridge.init(function (message, responseCallback) {
        console.log('JS got a message', message);
        var data = {
            'Javascript Responds': '测试中文!'
        };
        console.log('JS responding with', data);
        responseCallback(data);
    });

    webBridgeInit();
})

function webBridgeInit() {

    WebViewJavascriptBridge.registerHandler("cancelYinyue", function (data, responseCallback) {
        var responseData = "setFontFamily Javascript Says Right back aka! =";
        cancelYinyue();
    });


    WebViewJavascriptBridge.registerHandler("getHtmlContent", function (data, responseCallback) {
        var content = getHtmlContent();
        var responseData = "setFontFamily Javascript Says Right back aka! =";
        responseCallback(content);
    });
    WebViewJavascriptBridge.registerHandler("setFontFamily", function (fontFamily, responseCallback) {
        setFontFamily(fontFamily);
        var responseData = "setFontFamily Javascript Says Right back aka! =" + fontFamily;
        responseCallback(responseData);
    });

    WebViewJavascriptBridge.registerHandler("setFontAnimation", function (index, responseCallback) {
        setFontAnimation(index);
        var responseData = "Javascript Says Right back aka!";
        responseCallback(responseData);
    });

    WebViewJavascriptBridge.registerHandler("setFontColor", function (color, responseCallback) {
        setFontColor(color);
        var responseData = "Javascript Says Right back aka!";
        responseCallback(responseData);
    });

    WebViewJavascriptBridge.registerHandler("setFontSize", function (size, responseCallback) {
        setFontSize(size);
        var responseData = "Javascript Says Right back aka!";
        responseCallback(responseData);
    });

    WebViewJavascriptBridge.registerHandler("fontCancle", function (text, responseCallback) {
        fontCancle();
        var responseData = "Javascript Says Right back aka!";
        responseCallback(responseData);
    });

    WebViewJavascriptBridge.registerHandler("fontConfirm", function (text, responseCallback) {
        fontConfirm(text);
        var responseData = "Javascript Says Right back aka!";
        responseCallback(responseData);
    });

    WebViewJavascriptBridge.registerHandler("addFontAlign", function (text, responseCallback) {
        addFontAlign(text);
        var responseData = "Javascript Says Right back aka!";
        responseCallback(responseData);
    });

    WebViewJavascriptBridge.registerHandler("addTop", function (text, responseCallback) {
        var top = addTop(text);
        var responseData = "Javascript Says Right back aka!";
        responseCallback(top);
    });

    WebViewJavascriptBridge.registerHandler("addBottom", function (text, responseCallback) {
        var top = addBottom(text);
        var responseData = "Javascript Says Right back aka!";
        responseCallback(top);
    });


    WebViewJavascriptBridge.registerHandler("addAd", function (text, responseCallback) {
        var top = addAd(text);
        var responseData = "Javascript Says Right back aka!";
        responseCallback(top);
    });

}

