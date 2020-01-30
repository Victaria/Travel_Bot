package telegram.bot;

import org.hibernate.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@PropertySource("file:telegram.properties")
public class TravelBot extends TelegramLongPollingBot{


    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        if (message.equals("/start")) {
            sendMsg(update.getMessage().getChatId().toString(), "Привет! Я твой помощник в путешествиях!" + '\n'
                    + "Я здесь, чтобы помочь тебе узнать больше информации о городах мира!"
                    + '\n' + "Пожалуйста, введи имя города, который тебя интересует и убедить в правильности написания!"
                    + '\n' + "Например:  Минск");
        } else if (message.equals("/showAll")){
            sendMsg(update.getMessage().getChatId().toString(), getCities());
        } else {
            sendMsg(update.getMessage().getChatId().toString(), getCityByName(update.getMessage().getText()));
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
        session.close();
        return allCities;
    }

    public static String getCityByName(String name){
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
            if(city.getName().equals(name)) {
                session.close();
                return city.getDescription();
            }
        }
        session.close();
        return "Город не найден.";
    }
}
