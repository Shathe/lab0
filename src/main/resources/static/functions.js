var ws;
var client;
var subscription;

$(document).ready(function() {
	initConn();
});

var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope) {
	// Inicializar los tweets
    $scope.tweets= [];
    // si se pide una busqueda
    this.getTweets = function (){
		if(subscription != undefined){
			//si ya estabas subscrito a anteriores busquedas te desuscribes (solo una abierta a la vez)
			subscription.unsubscribe();
			$scope.tweets=[];
		}
		
		var query = $("#q").val();
		client.send("/app/search", {}, query);
		//Te subscribes a la busqueda. La funcion se ejecuta cuando el servidor envia algo por la cola subscrita
		subscription = client.subscribe("/queue/search/" + query, function(mensaje) {
			var data = JSON.parse(mensaje.body);
            $scope.tweets.unshift(data);
            $scope.$apply();
		}, {id: query});
		

      };
});

/*
 * Iniciarlizar la conexion con el servidor con el endpoint (del wwebsocket) llamado twitter
 */
function initConn() {
	ws = new SockJS("/twitter");
	client = Stomp.over(ws);
	var headers = {};
	var callback = {};
	client.connect(headers, callback);
}

