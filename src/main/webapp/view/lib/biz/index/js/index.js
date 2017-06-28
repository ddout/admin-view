/**
 * 
 */
define([ 'jquery', 'app', 'highcharts', 'text!biz/index/index.html' ], function($, app, highcharts, tpl) {
	var loadData = function(){
	};
	
	var index = {
		init : function() {
			app.mainContainer.html(tpl);
			loadData();
		}
	};

	return index;
});