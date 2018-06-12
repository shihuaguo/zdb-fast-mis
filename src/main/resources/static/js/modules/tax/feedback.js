$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'taxic/feedback/list',
        datatype: "json",
        colModel: [	
        	{ label: 'ID', name: 'id', width: 10, key: true, hidden: true },
        	{ label: '时间', name: 'feedbackTime', index: "feedback_time", width: 40,sortable:true},
        	{ label: '类别', name: 'type1', index: "type1", width: 20,formatter: function(value, options, row){
        		return zdb.format('feedbackType1',value,options,row);
        	},sortable:true},
			{ label: '反馈信息', name: 'feedbackInfo', width: 40,sortable:false},
			{ label: '办事小结', name: 'workSummary', width: 40,sortable:false},
			{ label: '办理人员', name: 'username', width: 20,sortable:false}
        ],
		viewrecords: true,
		width:	'100%',
        height: '100%',
        rowNum: 20,
		rowList : [20,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#zdb',
	data:{
		q:{
			feedbackInfo: null,
			type1:null
		},
		showList: true,
		title: '',
		feedbackType1List:[],
		vo:{
			id:null,
			feedbackTime: '',
			type1:'',
			feedbackInfo:'',
			workSummary:'',
			username:''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增信息反馈";
			vm.vo.id = null,
			vm.vo.feedbackTime = (new Date()).format("yyyy-MM-dd hh:mm:ss");
			vm.vo.type1 = '';
			vm.vo.feedbackInfo = '';
			vm.vo.workSummary = '';
			$("#feedbackInfo").html('');
			$("#workSummary").html('');
			
			//this.getFeedbackType1List();
			//this.getUsername();
		},
		update: function () {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改信息反馈";
            //this.getFeedbackType1List();
			//this.getUsername();
			this.getFeedback(id);
		},
		del: function () {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "taxic/feedback/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(){
                                vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		saveOrUpdate: function () {
            if(vm.validator()){
                return ;
            }
			var url = vm.vo.id == null ? "taxic/feedback/save" : "taxic/feedback/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.vo),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		getFeedback: function(id){
			$.get(baseURL + "taxic/feedback/getById/"+id, function(r){
				vm.vo = r.feedbackInfo;
				$("#feedbackInfo").html(r.feedbackInfo.feedbackInfo);
				$("#workSummary").html(r.feedbackInfo.workSummary);
				//vm.getTaskClassList(vm.task.taskType);
				/*if(!isBlank(vm.task.taskClass)){
					vm.getTaskSubClassList(vm.task.taskClass);
				}*/
			});
		},
		getFeedbackType1List: function(){
			vm.feedbackType1List = zdb.enum2arr('feedbackType1');
		},
		reload: function () {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'feedbackInfo': vm.q.feedbackInfo,'type1':vm.q.type1},
                page:page
            }).trigger("reloadGrid");
		},
        validator: function () {
            if(isBlank(vm.vo.feedbackTime)){
                alert("时间不能为空");
                return true;
            }
            if(isBlank(vm.vo.type1)){
                alert("事项类别不能为空");
                return true;
            }
            var feedbackInfo = $("#feedbackInfo").html();
            vm.vo.feedbackInfo = feedbackInfo;
            if(isBlank(vm.vo.feedbackInfo)){
            	alert("反馈信息不能为空");
            	return true;
            }
            
            var workSummary = $("#workSummary").html();
            vm.vo.workSummary = workSummary;
            if(isBlank(vm.vo.workSummary)){
            	alert("办事小结不能为空");
            	return true;
            }

        }
	}
});

vm.getFeedbackType1List();