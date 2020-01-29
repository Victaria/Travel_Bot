package telegram.bot;

import org.hibernate.*;
import org.hibernate.mapping.Map;
import org.hibernate.query.Query;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.*;
import java.util.List;

@Component
public class TravelBot extends TelegramLongPollingBot{
  //  private CityService service;

    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        if (message.equals("/start")) {
            sendMsg(update.getMessage().getChatId().toString(), "Hello! Привет! This is travel bot!" + '\n' + "I'm here to help you with learning information about world cities!"
                    + '\n' + "Please, enter name of city, that are you interesting for.");
        } else if (message.equals("/showAll")){
            sendMsg(update.getMessage().getChatId().toString(), getCities());
        }
    }

    public String getBotUsername() {
        return "travel_cities_bot";
    }

    public String getBotToken() {
        return "819420719:AAHVhV2Wvndgl4i_k0kG7NHcYKZcTS2SL0o";
    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sMg = new SendMessage();
        sMg.enableMarkdown(true);
        sMg.setChatId(chatId);
        sMg.setText(s);
        try {
            execute(sMg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            //log.log(Level.SEVERE, "Exception: ", e.toString());
        }
    }

    public static String getCities() {
        String allCities = "База городов: " + '\n';
        SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        Transaction tx = session.beginTransaction();
        SQLQuery query = session.createSQLQuery("select * from telegram_bot");
        List<Object[]> rows = query.list();
        for(Object[] row : rows){
            City city = new City();
            city.setId(Long.parseLong(row[0].toString()));
            city.setName(row[1].toString());
            city.setDescription(row[2].toString());
            allCities += city.getName() + "  "+ city.getDescription() + '\n';
        }
        return allCities;
    }
}
