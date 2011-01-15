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
