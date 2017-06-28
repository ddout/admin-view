/**
 * 
 */
define([ 'jquery', 'app',  'bootstrapTable', 'text!biz/security/depart.html' ], function($, app,bootstrapTable,tpl) {
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
		$('#btn-add').click(function(){
			app._function = 'add';
			app.loadPage('depart_view');
		});
		//
		var page = new app.Page({
			base:{
				url : app.rootPath + '/security/depart/list.do',//查询url
				from: '#search-form',//查询条件form
				toolbarAlign: '.search-toolbar',//工具条对象
				dataTarget: '#paginationTable',//分页数据table对象
				buildData : function(data){
					var pageData = [];
					$.each(data,function(i,item){
						pageData.push({
							id: item['ID'],
					        name: item['NAME'],
					        pname: item['PNAME'],
					        code:item['CODE'],
					        addcode:item['ADDCODE'],
					        contacts:item['CONTACTS'],
					        phone:item['PHONE'],
					        address:item['ADDRESS'],
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
			        field: 'pname',
			        title: '上级名称',
			        align:"left"
			    }, {
			        field: 'code',
			        title: '代码',
			        align:"left"
			    }, {
			        field: 'addcode',
			        title: '邮政编码',
			        align:"left"
			    }, {
			        field: 'contacts',
			        title: '联系人',
			        align:"left"
			    }, {
			        field: 'phone',
			        title: '联系电话',
			        align:"left"
			    }, {
			        field: 'address',
			        title: '地址',
			        align:"left",
			        formatter: totalNameFormatter
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
	            app.depart_id=row.id;
				app._function='edit';
				app.loadPage('depart_view');
	        },
	        'click .remove': function (e, value, row, index) {
	        	if(confirm("你确定要删除？")){
					app.ajax({
						url:app.rootPath + '/security/depart/delete.do',
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
	    
		
	}
			
	var depart = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
		}
	};

	return depart;
});