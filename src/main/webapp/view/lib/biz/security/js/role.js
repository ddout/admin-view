/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap','ztree', 'bootstrapTable', 'text!biz/security/role.html' ], function($, app, bootstrap, ztree, bootstrapTable, tpl) {
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
	function totalNameFormatter(data) {
		if(data){
			if(data.length > 10){
				return data.substr(0,10) + '...'
			}
		} else {
			return '-';
		}
    }
	
	var buildEvent = function(){
		//
		//
		var page = new app.Page({
			base:{
				url : app.rootPath + '/security/role/list.do',//查询url
				from: '#search-form',//查询条件form
				toolbarAlign: '.search-toolbar',//工具条对象
				dataTarget: '#paginationTable',//分页数据table对象
				buildData : function(data){
					var pageData = [];
					$.each(data,function(i,item){
						pageData.push({
							id: item['ID'],
					        name: item['NAME'],
					        sn: item['SN'],
					        description:item['DESCRIPTION'],
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
			        title: '名称',
			        align:"left"
			    }, {
			        field: 'sn',
			        title: '编码',
			        align:"left"
			    }, {
			        field: 'description',
			        title: '备注',
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
				app._function = 'UPDATEROLE';
				app.role_id = row.id;
				app.loadPage('role_view');
	        },
	        'click .remove': function (e, value, row, index) {
	        	if(confirm("你确定要删除？")){
					app.ajax({
						url:app.rootPath + '/security/role/delete.do',
						type:'POST',
						data:'ids='+row.id,
						success:function(data){
							alert('删除成功');
							page.reload();
					   }
					});
			    }
	        }
	    };
		//
		$('#btn-new').click(function(){
			app._function = 'ADDROLE';
			app.loadPage('role_view');
		});
		
		$('#ajax-demo').on('click',function(){
			var selections = page.getSelections();
			if(selections.length == 0){
				alert('未选择任何数据');
				return;
			} else if(selections.length > 1){
				alert("勾选一个用户");
				return;
			}
			var roleId = selections[0].id;
			app.ajax({
				url: app.rootPath + '/security/privilege/getTree.do',
				type: 'POST',
				data: 'roleId=' + roleId,
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
							    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
							    var node = treeObj.getNodeByTId(treeNode.tId);
							    treeObj.checkNode(node, true, true);
							}
						}
					};
					$('#ajax-modal input[name=roleId]').val(roleId);
					$('#ajax-modal').modal('show');
					$.fn.zTree.init($("#treeDemo"), setting, zNodes).expandAll(true);
				}
			});
		});
		$('#savePri').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getCheckedNodes(true);
			var ids = [];
			for(var i = 0; i < nodes.length; i++){
				ids[i] = nodes[i].id;
			}
			app.ajax({
				url: app.rootPath + '/security/role/savePrivilege.do',
				data: 'ids=' + ids + '&roleId=' + $('#ajax-modal input[name=roleId]').val(),
				success: function(data){
					alert('保存成功');
					$('#ajax-modal').modal('hide');
				}
			});
		});
	}
	
	var role = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
		}
	};
	return role;
});