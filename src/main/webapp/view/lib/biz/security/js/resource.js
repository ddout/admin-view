/**
 * 
 */
define(['jquery', 'app', 'jqueryForm', 'ztree', 'bootstrapTable', 'text!biz/security/resource.html'], function($, app, jqueryForm, ztree, bootstrapTable, tpl) {
	function operateFormatter(value, row, index) {
		return [
			'<a class="edit" href="javascript:void(0)" title="修改">',
			'<i class="glyphicon glyphicon-edit"></i>',
			'</a>  ',
			'<a class="remove" href="javascript:void(0)" title="删除">',
			'<i class="glyphicon glyphicon-trash"></i>',
			'</a>  '
		].join('');
	}
	//
	var buildEvent = function() {
		//
		var page = new app.Page({
			base: {
				url: app.rootPath + '/security/resource/listType.do', //查询url
				from: '#search_form_1', //查询条件form
				toolbarAlign: '#search_toolbar_1', //工具条对象
				dataTarget: '#paginationTable_1', //分页数据table对象
				buildData: function(data) {
					var pageData = [];
					$.each(data, function(i, item) {
						pageData.push({
							id: item['ID'],
							name: item['NAME'],
							code: item['CODE']
						});
					});
					return pageData;
				}
			},
			//
			table: {
				checkboxHeader: false,
				columns: [{
					field: 'ID',
					visible: false
				}, {
					field: 'name',
					title: '名称',
					align: "left"
				}, {
					field: 'code',
					title: '代码',
					align: "left"
				}, {
					field: 'operate',
					title: '操作',
					align: 'center',
					events: "operateEvents_1",
					formatter: operateFormatter
				}]
			}
		});
		//
		//
		var page2 = new app.Page({
			base: {
				url: app.rootPath + '/security/resource/listItems.do', //查询url
				from: '#search_form_2', //查询条件form
				toolbarAlign: '#search_toolbar_2', //工具条对象
				dataTarget: '#paginationTable_2', //分页数据table对象
				buildData: function(data) {
					var pageData = [];
					$.each(data, function(i, item) {
						pageData.push({
							id: item['ID'],
							name: item['NAME'],
							code: item['CODE'],
							val: item['VAL'],
							byorder: item['BYORDER'],
							mask: item['MASK']
						});
					});
					return pageData;
				}
			},
			//
			table: {
				checkboxHeader: false,
				columns: [{
					field: 'ID',
					visible: false
				}, {
					field: 'name',
					title: '名称',
					align: "left"
				}, {
					field: 'code',
					title: '代码',
					align: "left"
				}, {
					field: 'val',
					title: '值',
					align: "left"
				}, {
					field: 'byorder',
					title: '排序',
					align: "left"
				}, {
					field: 'mask',
					title: '说明',
					align: "left"
				}, {
					field: 'operate',
					title: '操作',
					align: 'center',
					events: "operateEvents_2",
					formatter: operateFormatter
				}]
			}
		});
		//
		window.operateEvents_1 = {
			'click .edit': function(e, value, row, index) {
				app.ajax({
					url:app.rootPath + '/security/resource/getTypeById.do',
					type:'POST',
					data:'id='+row.id,
					success:function(data){
						$('#ajax-modal-1 input').val('');
						$('#ajax-modal-1 select').val('');
						$('#ajax-modal-1 textarea').val('');
						for(var d in data){
							$('#ajax-modal-1 [name='+d.toLowerCase()+']').val(data[d]);
						}
						$('#ajax-modal-1 form').attr('action',app.rootPath+'/security/resource/updateType.do');
						$('#ajax-modal-1').modal('show');
					}
				});
			},
			'click .remove': function(e, value, row, index) {
				if(confirm("你确定要删除？")) {
					app.ajax({
						url: app.rootPath + '/security/resource/deleteType.do',
						data: 'ids=' + row.id,
						success: function(data){
							alert('删除成功');
							page.reload();
						}
					});
				}
			}
		};
		//
		window.operateEvents_2 = {
			'click .edit': function(e, value, row, index) {
				app.ajax({
					url:app.rootPath + '/security/resource/getItemsById.do',
					type:'POST',
					data:'id='+row.id,
					success:function(data){
						$('#ajax-modal-2 input').val('');
						$('#ajax-modal-2 select').val('');
						$('#ajax-modal-2 textarea').val('');
						for(var d in data){
							$('#ajax-modal-2 [name='+d.toLowerCase()+']').val(data[d]);
						}
						$('#ajax-modal-2 form').attr('action',app.rootPath+'/security/resource/updateItems.do');
						$('#ajax-modal-2').modal('show');
					}
				});
			},
			'click .remove': function(e, value, row, index) {
				if(confirm("你确定要删除？")) {
					app.ajax({
						url: app.rootPath + '/security/resource/deleteItems.do',
						type: 'POST',
						data: 'ids=' + row.id,
						success: function(data) {
							alert('删除成功');
							page2.reload();
						}
					});
				}
			}
		};
		//
		$('#paginationTable_1').on('click-row.bs.table', function(row, $element) {
			$('#sourceTypeId').val($element.id);
			$('#resource_type_2').html($element.name + '('+$element.code+')');
			page2.refresh({url:app.rootPath + '/security/resource/listItems.do',pid:$element.id,offset:1});
		});
		//
		$('#btn_add_1').on('click',function(){
			$('#ajax-modal-1 input').val('');
			$('#ajax-modal-1 select').val('');
			$('#ajax-modal-1 textarea').val('');
			$('#ajax-modal-1 form').attr('action',app.rootPath+'/security/resource/addType.do');
			$('#ajax-modal-1').modal('show');
		});
		$('#btn_add_2').on('click',function(){
			var pid = $('#sourceTypeId').val();
			if(pid!=null&&pid!=''&&pid!='-1'){
				$('#ajax-modal-2 input').val('');
				$('#ajax-modal-2 select').val('');
				$('#ajax-modal-2 textarea').val('');
				$('#ajax-modal-2 form').attr('action',app.rootPath+'/security/resource/addItems.do?pid='+pid);
				$('#ajax-modal-2').modal('show');
			}else{
				alert("选择资源类型！")
			}	
		});
		//
		//
		$("#ajax-modal-1 form").unbind('submit').on('submit', function(e) {
			e.preventDefault();
        	$(this).ajaxSubmit({
            	 success : function(dbdata){
            		var data = JSON.parse(dbdata);
            		if(data['result'] == 'SUCCESS'){
    					alert('操作成功');
    					$('#ajax-modal-1').modal('hide');
						page.reload();
    				} else if(data['result'] == 'ERROR'){
    					var msg = data['msg'];
    					if(null == msg){
        					alert('系统超时!');
        				} else {
        					alert(msg);
        				}
    				}
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
		$("#ajax-modal-2 form").unbind('submit').on('submit', function(e) {
			e.preventDefault();
        	$(this).ajaxSubmit({
            	 success : function(dbdata){
            		var data = JSON.parse(dbdata);
            		if(data['result'] == 'SUCCESS'){
    					alert('操作成功');
    					$('#ajax-modal-2').modal('hide');
						page2.reload();
    				} else if(data['result'] == 'ERROR'){
    					var msg = data['msg'];
    					if(null == msg){
        					alert('系统超时!');
        				} else {
        					alert(msg);
        				}
    				}
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
		//
		//
		$(window).resize(function() {
			$('#paginationTable_1').bootstrapTable('resetView');
			$('#paginationTable_2').bootstrapTable('resetView');
		});
	};
	//
	//
	var resource = {
		init: function() {
			app.mainContainer.html(tpl);
			buildEvent();
		}
	};
	return resource;
});