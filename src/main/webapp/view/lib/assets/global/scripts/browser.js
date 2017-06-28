var browser = {
	versions: function() {
		var u = navigator.userAgent,
			ua = navigator.userAgent
			.toLowerCase();
		return { //移动终端浏览器版本信息
			trident: u.indexOf('Trident') > -1, //IE内核
			presto: u.indexOf('Presto') > -1, //opera内核
			webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
			gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
			mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
			ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
			android: u.indexOf('Android') > -1 ||
				u.indexOf('Linux') > -1, //android终端或uc浏览器
			iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
			iPad: u.indexOf('iPad') > -1, //是否iPad
			webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
			wechat: ua.match(/MicroMessenger/i) == "micromessenger", //微信
			weibo: ua.match(/WeiBo/i) == "weibo", //微博
			qq: ua.match(/QQ/i) == "qq" //qq
		};
	}(),
	language: (navigator.browserLanguage || navigator.language)
		.toLowerCase()
};
//
function getBrowserInfo() {
	var agent = navigator.userAgent.toLowerCase();

	var regStr_ie = /msie [\d.]+;/gi;
	var regStr_ff = /firefox\/[\d.]+/gi
	var regStr_chrome = /chrome\/[\d.]+/gi;
	var regStr_saf = /safari\/[\d.]+/gi;
	//IE
	if(agent.indexOf("msie") > 0) {
		return agent.match(regStr_ie);
	}
	//firefox
	if(agent.indexOf("firefox") > 0) {
		return agent.match(regStr_ff);
	}
	//Chrome
	if(agent.indexOf("chrome") > 0) {
		return agent.match(regStr_chrome);
	}
	//Safari
	if(agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0) {
		return agent.match(regStr_saf);
	}
}
if(browser.versions['trident'] == true) {
	var version = parseFloat((getBrowserInfo() + "").replace(/[^0-9.]/ig, ""), 10);
	if(version < 8) {
		document.writeln('<div style="margin:0 auto;margin-top: 50px;font-size:18px;color:red;text-align: center;">您正在使用 IE' + version + '，该版本已经成为历史。建议您使用现代化浏览器：  <a href="http://www.google.com/chrome/?hl=zh-CN" target="_blank">Chrome</a> / <a href="http://www.mozillaonline.com/" target="_blank">Firefox</a> / <a href="http://www.apple.com.cn/safari/" target="_blank">Safari</a>，或升级<a href="http://www.microsoft.com/china/windows/internet-explorer/" target="_blank">IE</a>后在使用！</div>');
	}
}