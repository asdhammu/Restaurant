$(document).ready(function(){

	//featured dishes slider initialisation
	$("#dishes").carouFredSel({
		circular: true,
		infinite: false,
		auto 	: false,
		prev	: {	
			key		: "left"
		},
		next	: { 
			key		: "right"
		},
		pagination	: "#slider_nav"
	});






	//gallery hover effect
	$('.gallery .media img').hover(function(){
		$('.gallery .media img').addClass('inactive');
		$(this).removeClass('inactive').addClass('active');
	}, function(){
		$('.gallery .media img').removeClass('inactive');
	});






	//menu show more items btn
	function update_btn(old_btn_id, new_btn_id, btn_content){
		$(old_btn_id).removeAttr('id');
			$('.load-more a').attr({
				id : new_btn_id
			});
			$('#' + new_btn_id).html(btn_content);
	}


	$(document).on('click', 'a#more_items', function(event) {	    
		event.preventDefault();
		$('.menu .hidden_items').slideDown({duration:800,  easing: "easeInOutQuart", complete : function(){
			update_btn('#more_items', 'hide_items', 'hide items<hr/><span class="top_arrow"></span>');
		}});
	});
	

	$(document).on('click', 'a#hide_items', function(event) {	    
		event.preventDefault();		
		$('.menu .hidden_items').slideUp({duration:800,  easing: "easeInOutQuart", complete : function(){
			update_btn('#hide_items', 'more_items', 'show more<hr/><span class="bottom_arrow"></span>');
		}});	    
	});


});
