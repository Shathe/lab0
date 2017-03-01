package es.unizar.tmdad.lab0.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

@Service
public class TwitterLookupService {
	@Value("${twitter.consumerKey}")
	private String consumerKey;

	@Value("${twitter.consumerSecret}")
	private String consumerSecret;

	@Value("${twitter.accessToken}")
	private String accessToken;

	@Value("${twitter.accessTokenSecret}")
	private String accessTokenSecret;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	private Map<String, Stream> map = new HashMap<>();

	/*
	 * Cada vez que llega una peticion de subscripcion
	 */
	public void search(String q) {

		// Si la cola de esa query esta abierta no hace falta abrir una nueva
		if (!map.containsKey(q)) {
			// Si la query no se esta buscando ya se inserta en el mapa
			if (map.size() >= 10) {
				// Como maximo debe haber 10 queries buscando a la vez
				// El criterio de eliminación es el más viejo, FIFO
				Object firstKey = map.keySet().toArray()[0];
				map.get(firstKey).close();
				map.remove(firstKey);
			}

			// Se crea un listener para la query
			Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
			List<StreamListener> list = new ArrayList<StreamListener>();
			list.add(new SimpleStreamListener(messagingTemplate, q));
			// creas el nuevo stream 
			Stream str = twitter.streamingOperations().filter(q, list);
			// Insertar el stream (con la key = query)
			map.put(q, str);
		}
	}
}