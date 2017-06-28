/**
 * -----------
 */
var pageConfig = {
		"index" : {"js":"biz/index/js/index"},
		"admin_sidebar" : {"js":"biz/index/js/admin_sidebar"}
};
define([ "jquery", "hashchange", "bootstrap", "jqueryForm", "css!app.css"], function($, h, bootstrap, jqueryForm, css ) {
	changeLoadPage = function(key) {
		var js = '';
		if (!pageConfig.hasOwnProperty(key)) {
			var keys = key.split('_');
			js =  pageConfig[keys[0]]['js'].substr(0, pageConfig[keys[0]]['js'].indexOf('.js')) 
			   + "_" + keys[1] +".js";
		} else {
			js = pageConfig[key]['js'];
		}
		app.mainContainer.html(app.loadingImg);
		require([js], function(js, html) {
			window.operateEvents = null;
			js.init();
			Layout.windowResize();
		});
	};
	function getRootPath() {
		var pathName = window.location.pathname.substring(1);
		var webName = pathName == '' ? '' : pathName.substring(0, pathName
				.indexOf('/'));
		var a = window.location.protocol + '//' + window.location.host + '/'
				+ webName;
		return a;
	};
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
	var getBrowserInfo = function () {
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
	};

	var app = {
		init : function() {
			require([pageConfig['admin_sidebar']['js']], function(admin_sidebar) {
				admin_sidebar.init();
				$('#admin_sidebar').on('click', 'a', function() {
					var key = $(this).attr('target-url');
					//active
					if (key &&  key != '') {
						$('#admin_sidebar li').removeClass('active');
						$(this).parent('li').parent('ul').parent('li').addClass('active');
						app.loadPage(key);
						$('a[data-target=".navbar-collapse"]').click();
					}
				});
			});
			$(window).hashchange(function() {
				if (location.hash) {
					changeLoadPage(location.hash.substr(1));
				}
			});
		},
		loadPage : function(key) {
			self.location.hash = key;
			//$(window).hashchange();
		},
		loadingImg : 'loading.......',
		rootPath : getRootPath(),
		mainContainer : $("#admin-content"),
		appUser : null,
		
		browser : function(){return browser;},
		getBrowserInfo: getBrowserInfo(),
		
		ajax : function(ajax_config){
	    	var _ajax_config = {
	    			url : '',
	    			type : 'POST',
	    			dataType : 'json',
	    			async : 'false',
	    			data : '',
	    			success : function(data) {},
	    			error : function(msg) {
	    				if(null == msg){
	    					alert('发送错误!');
	    				} else {
	    					alert(msg);
	    				}
	    			}
	    	};
	    	$.extend(_ajax_config, ajax_config);
	    	$.ajax({
				url : _ajax_config.url,
				type : _ajax_config.type,
				dataType : _ajax_config.dataType,
				async : _ajax_config.async,
				data : _ajax_config.data,
				success : function(data) {
					if(data['result'] == 'SUCCESS'){
						_ajax_config.success(data['rows']);
					} else if(data['result'] == 'ERROR'){
						_ajax_config.error(data['msg']);
					}
					Layout.windowResize();
				},
				error : function(xhr, errorType, error) {
					if(xhr['status'] == '600'){
						alert('未登录!');
						window.location.href = "login.html";
					} else if(xhr['status'] == '401'){
						alert('您没有该操作的访问权限!');
					} else {
						_ajax_config.error('出错了! error:'  + xhr['status']);
					}
				}
			});
	    },
		//
		Page : function(_config){
			var _this = this;
			var cfg = {
					base:{
						url : '',//查询url
						from: '#search-form',//查询条件form
						buildData : function(data){return [];},//构建数据方法
						after : function(){},//执行查询前执行
						before : function(){},//查询成功后执行
						//
						dataTarget: '#paginationTable',//分页数据table对象
					},
					table:{
						cardView: browser.versions.mobile == true ? true : false,
						height:550,//默认高度
						pagination: true,//启用分页
						paginationLoop: false,//分页循环
						pageList:[10, 25, 30, 50],//分页默认limit
						sidePagination:"server",//分页方式
						undefinedText:'-',//空数据显示
						checkboxHeader:'true',//是否显示checkbox
						striped:'true',//隔行颜色
						toolbarAlign: '#toolbar',//工具条对象
					    columns: []//列对象
					}
			};
			$.extend(cfg.base, _config.base);
			$.extend(cfg.table, _config.table);
			//limit: params.data.limit,
			//start: params.data.offset/params.data.limit + 1
			//'start=&limit='
			$(cfg.base.from).append('<input type="hidden" name="start" value="1"/><input type="hidden" name="limit" value="10"/>');
			$(cfg.base.from).append('<input type="hidden" name="t" value="-1"/>');
			var _params = {}, startFlag = false;
			var formsubmit = function(params){//分页加载数据的重写ajax
				startFlag = true;
				var limit = params.data.limit ? params.data.limit : 10;
				var start = params.data.offset ? params.data.offset/params.data.limit + 1 : 1;
				$(cfg.base.from).find('input[name=start]').val(start);
            	$(cfg.base.from).find('input[name=limit]').val(limit);
            	$(cfg.base.from).find('input[name=t]').val(new Date().getTime());
            	_params = params;
            	$(cfg.base.from).submit();
			}
			$(cfg.base.from).unbind('submit').attr('action', cfg.base.url).on('submit', function(e) {
				e.preventDefault();
				if(startFlag == false){
					$(cfg.base.dataTarget).bootstrapTable('selectPage',1);
					return;
				} else {
					startFlag = false;
				}
				cfg.base.before();
	            //
            	$(this).ajaxSubmit({
	            	 success : function(dbdata){
	            		var data = JSON.parse(dbdata);
	            		if(data['result'] == 'SUCCESS'){
	    					var pageData = data['rows'];
	    					var count = parseInt(data['total'], 10);
	    					_params.success({
	    			            total: count,
	    			            rows: cfg.base.buildData(pageData)
	    			        });
	    					Layout.windowResize();
	    				} else if(data['result'] == 'ERROR'){
	    					var msg = data['msg'];
	    					if(null == msg){
	        					alert('系统超时!');
	        				} else {
	        					alert(msg);
	        				}
	    				}
	 					//执行回调
	 					cfg.base.after();
	 					Layout.windowResize();
		      	    },
		      	    error : function(xhr, errorType, error) {
		      	    	$('#my-modal-loading').modal('close');
						if(xhr['status'] == '600'){
							alert('未登录!');
							window.location.href = "login.html";
						} else if(xhr['status'] == '401'){
							alert('您没有该操作的访问权限!');
						} else {
							alert('出错了! error:'  + xhr['status']);
						}
					}
	            });
	        });
			cfg.table.ajax = formsubmit;
			$(cfg.base.dataTarget).bootstrapTable(cfg.table);
			//
			return {
				reload : function(){
					$(cfg.base.dataTarget).bootstrapTable('refresh');
				},
				refresh : function(params){
					$(cfg.base.dataTarget).bootstrapTable('refresh', params);
				},
				goPageOne : function(){
					$(cfg.base.dataTarget).bootstrapTable('selectPage',1);
				},
				getSelections : function(){
					return $(cfg.base.dataTarget).bootstrapTable('getSelections');
				}
			};
		}
	};

	return app;
});