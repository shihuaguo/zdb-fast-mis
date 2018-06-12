zdb_enums = {
	taskType : {
		'1' : '工商',
		'2' : '税务',
		'9' : '其他'
	},
	taskClass : {
		'11' : '开业',
		'12' : '变更',
		'13' : '注销',
		'19' : '其他',
		'21' : '发票',
		'22' : '税务登记',
		'23' : '申报',
		'24' : '文书受理',
		'25' : '其他'
	},
	taskSubClass : {
		'211' : '领发票',
		'212' : '代开发票',
		'221' : '国税报到',
		'222' : '地税报到',
		'223' : '开通社保',
		'224' : '变更',
		'225' : '注销',
		'231' : '前台申报',
		'232' : '更正申报',
		'241' : '票种核定',
		'242' : '申请一般纳税人（含最好开票限额）',
		'243' : '纳税证明'
	},
	feedbackType1 : {
		'tax' : '税务事项反馈',
		'ic' : '工商事项反馈',
		'other' : '其他事项反馈'
	}
}
  zdb={};

/**
 * 根据枚举类型,枚举的key值获取value
 * 例如zdb.format('taskType','1')='工商'
 */
  zdb.format=function(_target,v,o,r){
	  var value="-",target=null,_index= v || '';
		for(var o in zdb_enums){
			target = (o.toString() == _target.toString() ? zdb_enums[o] : null);
			if(target !=null) break;
		}  
		if(target ==null) return value;
		for(var o in target){
			if(o.toString() == _index.toString()){
				value= target[o];  
				break;
			}
		}    
		return value;
  }
  
  /**
   * 将枚举类型转化为数组,用户下拉框显示
   */
  zdb.enum2arr=function(_target){
	  var value=[],target=null;
		for(var o in zdb_enums){
			target = (o.toString() == _target.toString() ? zdb_enums[o] : null);
			if(target !=null) break;
		}  
		if(target ==null) return value;
		for(var o in target){
			value.push({'label':target[o],'value':o});
		}    
		return value;
  }
  
  zdb.taskClassList=function(taskType){
	  var value=[],target=null;
		for(var o in zdb_enums.taskClass){
			if(o.indexOf(taskType) == 0){
				value.push({'label':zdb_enums.taskClass[o],'value':o});
			}
		}    
		return value;
  }
  
  zdb.taskSubClassList=function(taskClass){
	  var value=[],target=null;
	  for(var o in zdb_enums.taskSubClass){
		  if(o.indexOf(taskClass) == 0){
			  value.push({'label':zdb_enums.taskSubClass[o],'value':o});
		  }
	  }    
	  return value;
  }