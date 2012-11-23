if (window.innerWidth && window.innerWidth <= 480) {
    $(document).ready(function(){ 
        $('#navlinks ul').addClass('hide'); 
        $('#header').append('<div class="leftButton" onclick="toggleMenu()">Menu</div>');
    }); 
    function toggleMenu() {
        $('#navlinks ul').toggleClass('hide'); 
        $('#header .leftButton').toggleClass('pressed');
    }
}

