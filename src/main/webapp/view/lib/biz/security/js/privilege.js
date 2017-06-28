/**
 * 
 */
define([ 'jquery', 'app', 'bootstrapTable', 'text!biz/security/privilege.html' ], function($, app, bootstrapTable, tpl) {

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
	
	function ptypeFormatter(data) {
		//类型(默认为1;1=菜单;2=按钮;3=接口)
		if(data == 1){
			return '菜单';
		} else if(data == 2){
			return '按钮';
		} else if(data == 3){
			return '接口';
		} else {
			return '-';
		}
    }
	var buildEvent = function(){
		//
		//
		var page = new app.Page({
			base:{
				url : app.rootPath + '/security/privilege/list.do',//查询url
				from: '#search-form',//查询条件form
				toolbarAlign: '.search-toolbar',//工具条对象
				dataTarget: '#paginationTable',//分页数据table对象
				buildData : function(data){
					var pageData = [];
					$.each(data,function(i,item){
						pageData.push({
							id: item['ID'],
							pname: item['PNAME'],
					        name: item['NAME'],
					        url:item['URL'],
					        icon:item['ICON'],
					        orderby:item['ORDERBY'],
					        ptype:item['PTYPE']
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
			        field: 'pname',
			        title: '上级名称',
			        align:"left"
			    }, {
			        field: 'name',
			        title: '名称',
			        align:"left"
			    }, {
			        field: 'url',
			        title: 'URL',
			        align:"left"
			    }, {
			        field: 'icon',
			        title: 'ICON',
			        align:"left"
			    }, {
			        field: 'orderby',
			        title: 'ORDERBY',
			        align:"left"
			    }, {
			        field: 'ptype',
			        title: 'PTYPE',
			        align: "left",
			        formatter: ptypeFormatter
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
				app.privilege_id=row.id;
				app._function='edit';
				app.loadPage('privilege_view');
	        },
	        'click .remove': function (e, value, row, index) {
	        	if(confirm("你确定要删除？")){
					app.ajax({
						url:app.rootPath + '/security/privilege/delete.do',
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
		$('#btn-add').click(function(){
			app._function='add';
			app.loadPage('privilege_view');
		});
		
	}
			
	var privilege = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
		}
	};

	return privilege;
});