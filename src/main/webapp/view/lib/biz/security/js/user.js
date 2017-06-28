/**
 * 
 */
define([ 'jquery', 'app','ztree', 'bootstrapTable','text!biz/security/user.html' ], function($, app, ztree, bootstrapTable, tpl) {
	function operateFormatter(value, row, index) {
        return [
            '<a class="edit" href="javascript:void(0)" title="修改">',
            '<i class="glyphicon glyphicon-edit"></i>',
            '</a>  ',
            '<a class="refresh" href="javascript:void(0)" title="密码重置">',
            '<i class="glyphicon glyphicon-refresh"></i>',
            '</a>  ',
        ].join('');
    }
	function totalNameFormatter(data) {
		if(data == '-1'){
			return '是';
		}else if(data == '0'){
			return '否';
		} else {
			return '否';
		}
    }
	
		
	var buildEvent = function(){
		//
		var page = new app.Page({
			base:{
				url : app.rootPath + '/security/user/list.do',//查询url
				from: '#search-form',//查询条件form
				toolbarAlign: '.search-toolbar',//工具条对象
				dataTarget: '#paginationTable',//分页数据table对象
				buildData : function(data){
					var pageData = [];
					$.each(data,function(i,item){
						pageData.push({
							id: item['ID'],
					        name: item['NAME'],
					        gname: item['GNAME'],
					        username:item['USERNAME'],
					        email:item['EMAIL'],
					        phone:item['PHONE'],
					        departname:item['DEPARTNAME'],
					        enabled:item['ENABLED'],
					        createtime:item['CREATETIME']
						});
					});
					return pageData;
				}
			},
			//
			table:{
				columns: [{
			        field: 'ID',
			        title: 'id',
			        checkbox: true
			    }, {
			        field: 'name',
			        title: '真实姓名',
			        align:"left"
			    }, {
			        field: 'gname',
			        title: '性别',
			        align:"left"
			    }, {
			        field: 'username',
			        title: '登录账户',
			        align:"left"
			    }, {
			        field: 'email',
			        title: '电子邮件',
			        align:"left"
			    }, {
			        field: 'phone',
			        title: '联系电话',
			        align:"left"
			    }, {
			        field: 'departname',
			        title: '所属机构',
			        align:"left"
			    }, {
			        field: 'enabled',
			        title: '是否禁用',
			        align:"left",
			        formatter: totalNameFormatter
			    }, {
			        field: 'createtime',
			        title: '创建时间',
			        align:"right"
			    }, {
	                field: 'operate',
	                title: '操作',
	                align: 'center',
	                events: "operateEvents",
	                formatter: operateFormatter
	            }]
			}
		});
	    window.operateEvents = {
	        'click .edit': function (e, value, row, index) {
				app.user_id = row.id;
				app._function='edit';
				app.loadPage('user_view');
	        },
	        'click .refresh': function (e, value, row, index) {
	        	if (confirm("确认重置密码？")) {
					app.ajax({
						url : app.rootPath + '/security/login/resetPassword.do',
						data : 'id='+ row.id,
						success : function(data) {
							alert('重置成功，恢复初始密码 "111111"');
						}
					});
				}
	        }
	    };
	    //
		
		$('#btn-new').on('click',function(){
			app._function='add';
			app.loadPage('user_view');
		});
		//
		$('#btn-start').on('click',function(){
			var selections = page.getSelections();
			if(selections.length == 0){
				alert('未选择任何数据');
				return;
			}
			if(confirm("确定启用？")){
				var ids = [];
				$.each(selections, function(i,item){
					ids.push(item.id);
				});
				app.ajax({
					url: app.rootPath + '/security/user/invokeByStart.do',
					data: 'ids=' + ids,
					success: function(data){
						page.reload();
					}
				});
			}
		});
		$('#btn-del').on('click',function(){
			var selections = page.getSelections();
			if(selections.length == 0){
				alert('未选择任何数据');
				return;
			}
			if(confirm("确定禁用？")){
				var ids = [];
				$.each(selections, function(i,item){
					ids.push(item.id);
				});
				app.ajax({
					url: app.rootPath + '/security/user/delete.do',
					data: 'ids=' + ids,
					success: function(data){
						page.reload();
					}
				});
			}
		});
		//设置角色 显示角色树形结构
		$('#showrole').click(function(){
			var selections = page.getSelections();
			if(selections.length == 0){
				alert('未选择任何数据');
				return;
			} else if(selections.length > 1){
				alert("勾选一个用户");
				return;
			}
			var id = selections[0].id;
			app.ajax({
				url: app.rootPath + '/security/role/getTree.do',
				type: 'POST',
				data: 'userId=' + id,
				dataType: 'json',
				success: function(data){
					var zNodes = [];
					var temp ;
					$.each(data,function(i,item){
						if(item['CHECKED'] == 'true'){
							temp = {id:item['ID'], pId: item['PID'], name: item['NAME'], checked: true };
						}else{
							temp = {id:item['ID'], pId: item['PID'], name: item['NAME'] };
						}
						zNodes.push(temp);
					});
					var setting = {
						check: {
							enable: true,
							chkStyle: "checkbox",
							chkboxType: { "Y": "ps", "N": "ps" }
						},
						data: {  
			                simpleData: {  
			                    enable: true,
			                    idKey: "id",
			        			pIdKey: "pId"
			                }
			            },
			            callback: {
			            	beforeClick: function zTreeOnClick(treeId, treeNode) {
							    var treeObj = $.fn.zTree.getZTreeObj("tree_2");
							    var node = treeObj.getNodeByTId(treeNode.tId);
							    treeObj.checkNode(node, true, true);
							}
						}
					};
					$('#ajax-modal input[name=userid]').val(id);
					$('#ajax-modal').modal('show');
					$.fn.zTree.init($("#tree_2"), setting, zNodes).expandAll(true);
				}
			});
		});
		
		//模态框 保存设置角色
		$('#save-role').click(function(){
			var treeObj = $.fn.zTree.getZTreeObj("tree_2");
			var nodes = treeObj.getCheckedNodes(true);
			var roleIds=[];
			for(var i=0; i<nodes.length; i++){
				roleIds.push(nodes[i].id);
			}
			app.ajax({
				url: app.rootPath + '/security/user/saveRole.do',
				type: 'POST',
				data: 'roleIds=' + roleIds+'&userId='+$('#ajax-modal input[name=userid]').val(),
				dataType: 'json',
				success: function(data){
					$('#ajax-modal').modal('hide');
					alert('保存成功');
				}
			});
			
		});
		
		//数据权限-显示部门树形结构
		$('#showdepart').click(function(){
			var selections = page.getSelections();
			if(selections.length == 0){
				alert('未选择任何数据');
				return;
			} else if(selections.length > 1){
				alert("勾选一个用户");
				return;
			}
			var id = selections[0].id;
			
			app.ajax({
				url: app.rootPath + '/security/depart/getTree.do',
				type: 'POST',
				data: 'userId=' + id+ '&type=tree',
				dataType: 'json',
				success: function(data){
					var zNodes = [];
					var temp ;
					$.each(data,function(i,item){
						if(item['CHECKED'] == 'true'){
							temp = {id:item['ID'], pId: item['PID'], name: item['NAME'], checked: true };
						}else{
							temp = {id:item['ID'], pId: item['PID'], name: item['NAME'] };
						}
						zNodes.push(temp);
					});
					var setting = {
						check: {
							enable: true,
							chkStyle: "checkbox",
							chkboxType: { "Y": "s", "N": "s" }
						},
						data: {  
			                simpleData: {  
			                    enable: true,
			                    idKey: "id",
			        			pIdKey: "pId"
			                }
			            },
			            callback: {
			            	beforeClick: function zTreeOnClick(treeId, treeNode) {
							    var treeObj = $.fn.zTree.getZTreeObj("tree_3");
							    var node = treeObj.getNodeByTId(treeNode.tId);
							    treeObj.checkNode(node, true, true);
							}
						}
					};
					$('#ajax-modal1 input[name=userid]').val(id);
					$.fn.zTree.init($("#tree_3"), setting, zNodes).expandAll(true);
				}
			});
			$('#ajax-modal1').modal('show');
		});
		//保存
		$('#save-depart').click(function(){
			var treeObj = $.fn.zTree.getZTreeObj("tree_3");
			var nodes = treeObj.getCheckedNodes(true);
			var departIds=[];
			for(var i=0; i<nodes.length; i++){
				departIds.push(nodes[i].id);
				}
			app.ajax({
				url: app.rootPath + '/security/user/saveDepart.do',
				type: 'POST',
				data: 'departIds=' + departIds+'&userId='+$('#ajax-modal1 input[name=userid]').val(),
				dataType: 'json',
				success: function(data){
					$('#ajax-modal1').modal('hide');
					alert('保存成功');
				}
			});
		});
	}

	var user = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
		}
	};
	return user;
});