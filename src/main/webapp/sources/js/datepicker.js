    $(function(){
	$("#datepicker").datepicker({
		minDate: null
	});
});

$.datepicker.regional['ru'] = {
	dateFormat: 'dd.mm.yy'
};
$.datepicker.setDefaults($.datepicker.regional['ru']);

	$(function(){
		$("#datepicker2").datepicker({
			minDate: null
		});
	});