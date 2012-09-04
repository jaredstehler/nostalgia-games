!function($){
	$(function(){
		var games = {
				'streetrider': {
					code: 'StreetRider.class',
					title: 'Street Rider',
					width: 300,
					height: 280
				},
				'dirtracer': {
					code: 'DirtRacer.class',
					title: 'Dirt Racer',
					width: 280,
					height: 300
				},
				'punishtheposer': {
					code: 'PunishThePoser.class',
					title: 'Punish the Poser',
					width: 420,
					height: 300
				},
				'xup': {
					code: 'XUp.class',
					title: 'X-Up!',
					width: 418,
					height: 400
				},
				'whackamole': {
					code: 'WhackaMole.class',
					title: 'Whack-a-Mole',
					width: 300,
					height: 330
				},
				'breakout': {
					code: 'BreakOut.class',
					title: 'Breakout',
					width: 360,
					height: 380
				}
		}
		
		function launchGame(ev) {
			var el = $(ev.target),
			    gameName = el.data('game'),
			    game = games[gameName],
			    $gameContainer = $('.game-container');
			
			$gameContainer
				.css({width: game.width, height: game.height + 90})
				.find('h2').html(game.title);

			avgrund.activate();

			setTimeout(function(){ renderGame($gameContainer, game) }, 10);
		}

		function renderGame($container, game) {
			$('<embed>').attr('code', game.code)
			  .attr('width', game.width)
			  .attr('height', game.height)
			  .attr('type', 'application/x-java-applet')
			  .attr('pluginspage', 'http://java.sun.com/j2se/1.5.0/download.html')
			  .appendTo( $container.find('.content') );
		}
		
		function gameClosed(ev) {
			$('.game-container .content').html('');
		}
		
		$('.play').on('click', launchGame);
		$('.game-container').on('closed', gameClosed);		
	});
	
}(window.jQuery);
