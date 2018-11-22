package br.com.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import javax.jms.JMSException;
import javax.jms.Session;
import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueSender;
import com.ibm.mq.jms.MQQueueSession;

@SuppressWarnings({ "unused", "deprecation" })
public class Sender {
	
	public static void main(String[] args) {

		try {
			
			//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
			MQQueueConnectionFactory cf = new MQQueueConnectionFactory();

			String queueName = "xxxx";
			cf.setHostName("192.168.XX.XX");
			cf.setPort(1416);
			cf.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
			cf.setQueueManager("xxxxx");
			cf.setChannel("xxxx");

			MQQueueConnection connection = (MQQueueConnection) cf.createQueueConnection("xxx","xxx");
			MQQueueSession session = (MQQueueSession) connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			MQQueue queue = (MQQueue) session.createQueue("queue:///"+queueName);
			MQQueueSender sender =  (MQQueueSender) session.createSender(queue);
			     
			// Start the connection
			connection.start();
			
			for(int i=1; i<=360000; i++){
				List<String> listData = generateMsg(25);
				for(String linha : listData){
					JMSTextMessage message = (JMSTextMessage) session.createTextMessage(linha);
					System.out.println(linha);
					sender.send(message);
				}
				Thread.sleep(1000);
			}

			sender.close();
			session.close();
			connection.close();

			System.out.println("SUCCESS");
		}

		catch (JMSException jmsex) {
			System.out.println(jmsex);
			System.out.println("FAILURE");
		}
		
		catch (Exception ex) {
			System.out.println(ex);
			System.out.println("FAILURE");
		}
	}

	private static List<String> generateMsg(int total) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		Random r = new Random();
		List<String> list = new ArrayList<String>();
		for(int i=0;i<total;i++) {
			list.add(sdf.format(new Date())+(r.nextInt()*100));
		}
		return list;
	}
	
}
