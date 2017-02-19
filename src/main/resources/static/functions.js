$(document).ready(function() {
	registerSearch();
});

var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope) {
    $scope.tweets= [];
    
    this.getTweets = function (){
        $.get('/search',{q: $("#q").val()})
        .done(function(data, status) {
  		    console.log(data);
            $scope.tweets=data;
            $scope.$apply();
        })
        .fail(function(data, status) {
          console.log(data);
        });
      };
});

function registerSearch() {
	$("#search").submit(function(ev){
		event.preventDefault();
		$.get($(this).attr('action'), {q: $("#q").val()}, function(data) {
			$("#resultsBlock").empty().append(data);
		});	
	});
}

