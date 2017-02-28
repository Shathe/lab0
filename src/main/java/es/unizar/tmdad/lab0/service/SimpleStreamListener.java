package es.unizar.tmdad.lab0.service;
 
 import java.util.HashMap;
 import java.util.Map;
 
 import org.springframework.messaging.MessageHeaders;
 import org.springframework.messaging.simp.SimpMessageSendingOperations;
 import org.springframework.social.twitter.api.StreamDeleteEvent;
 import org.springframework.social.twitter.api.StreamListener;
 import org.springframework.social.twitter.api.StreamWarningEvent;
 import org.springframework.social.twitter.api.Tweet;
 import org.springframework.util.MimeTypeUtils;
 
 public class SimpleStreamListener implements StreamListener {
 
 	private SimpMessageSendingOperations smso;
 	private String query;
 	
 	public SimpleStreamListener(SimpMessageSendingOperations smso, String query) {
 		this.smso = smso;
 		this.query = query;
 	}
 	
 	@Override
 	public void onTweet(Tweet tweet) {
 		// Envias el tweet por la cola de subscripciones
 		Map<String,Object> map = new HashMap<>();
 		map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
 		smso.convertAndSend("/queue/search/" + query, tweet, map);
 	}
 
 	@Override
 	public void onDelete(StreamDeleteEvent deleteEvent) {
 		// TODO Auto-generated method stub
 		
 	}
 
 	@Override
 	public void onLimit(int numberOfLimitedTweets) {
 		// TODO Auto-generated method stub
 		
 	}
 
 	@Override
 	public void onWarning(StreamWarningEvent warningEvent) {
 		// TODO Auto-generated method stub
 		
 	}
 
 }