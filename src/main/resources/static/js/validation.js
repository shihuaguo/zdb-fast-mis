/**
 * 日期格式校验 yyyy-MM-dd HH
 * @param str
 * @returns
 */
function isValidDateWithHH(str) { 
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2})$/; 
	var r = str.match(reg); 
	if(r == null)return false; 
	return true;
}