// ==============================================================
// 方法名: comfirmDeleteProblem
// 方法描述: 本方法主要实现确认Problem的删除
// 返回值：
// false: 不提交请求
// ==============================================================
function comfirmDeleteProblem(id){
	if (confirm("Sure to delete this problem?")){
		location = 'problem/deleteProblem.action?id=' + id;
	}
}

//==============================================================
//方法名: comfirmDeleteContest
//方法描述: 本方法主要实现确认Contest的删除
//返回值：
//false: 不提交请求
//==============================================================
function comfirmDeleteContest(id){
	if (confirm("Sure to delete this contest?")){
		location = 'contest/deleteContest.action?cid=' + id;
	}
}

//==============================================================
//方法名: toggleOpen
//方法描述: 本方法实现切换源代码公开性
//返回值：
//==============================================================
function toggleOpen(id, flag){
	if (!flag){
		location = 'problem/toggleOpen.action?id=' + id;
	} else {
		location = 'contest/toggleOpen.action?id=' + id;
	}
}


/**
 * 为字符串增加trim函数
 */
if(typeof String.prototype.trim !== 'function') {
	String.prototype.trim = function() {
		return this.replace(/^\s+|\s+$/g, '');
	}
}
