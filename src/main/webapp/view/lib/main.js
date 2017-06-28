

( function(win) {
	require.config({
		urlArgs: "bust=" + new Date().getTime(),
		baseUrl : 'view/lib/',
		paths : {
			"jquery" : "assets/global/plugins/jquery.min",
			"hashchange" : "assets/global/plugins/jquery.ba-hashchange.min",
			"jqueryQuery" : "jassets/global/plugins/jquery.query",
			"jqueryForm" : "assets/global/plugins/jquery.form",
			"bootstrap" : "assets/global/plugins/bootstrap/js/bootstrap.min",
			"bootstrapTableCode" : "assets/global/plugins/bootstrap-table/bootstrap-table.min",
			"bootstrapTable" : "assets/global/plugins/bootstrap-table/bootstrap-table-zh-CN.min",
			"App" : "assets/global/scripts/app.min",
			"quickSidebar" : "assets/layouts/global/scripts/quick-sidebar.min",
			"layout" : "assets/layouts/layout2/scripts/layout.min",
			"ztree" : "assets/global/plugins/zTree_v3/js/jquery.ztree.all-3.5.min",
			"My97DatePicker":"assets/global/plugins/My97DatePicker/WdatePicker",
			"highcharts":"assets/global/plugins/highcharts/js/highcharts",
			"md5":"assets/global/scripts/jQuery.md5",
			"app" : "app/app"
		},
		
		shim : {
			"jqueryQuery" : {
				deps : [ "jquery" ],
				exports : "jqueryQuery"
			},
			"jqueryForm" : {
				deps : [ "jquery" ],
				exports : "jqueryForm"
			},
			"hashchange" : {
				deps : [ "jquery" ],
				exports : "hashchange"
			},
			"App" : {
				deps : [ "jquery", "bootstrap" ],
				exports : "App"
			},
			"quickSidebar" : {
				deps : [ "App" ],
				exports : "quickSidebar"
			},
			"bootstrap" : {
				deps : [ "jquery" ],
				exports : "bootstrap"
			},
			"bootstrapTable" : {
				deps : [ "jquery", "bootstrapTableCode", "css!assets/global/plugins/bootstrap-table/bootstrap-table.min.css" ],
				exports : "bootstrapTable"
			},
			"layout" :{
				deps : [ "jquery", "App"],
				exports : "layout"
			},
			"ztree" : {
				deps : [ "jquery"],
				exports : "ztree"
			},
			"My97DatePicker": {
				deps : [ "jquery"],
				exports : "My97DatePicker"
			},
			"highcharts": {
				deps : [ "jquery"],
				exports : "highcharts"
			}
		}
	});
	require([ "jquery", "bootstrap", "App", "quickSidebar", "layout" ], function($, bootstrap, App, quickSidebar, layout) {
		$.browser={};(function(){$.browser.msie=false; $.browser.version=0;if(navigator.userAgent.match(/MSIE ([0-9]+)./)){ $.browser.msie=true;$.browser.version=RegExp.$1;}})();
		$FF = null;
		self.location.hash = '';
		//$(window).hashchange();
		require(["app"], function(app) {
			app.init();
		});
	});
	
})(window);