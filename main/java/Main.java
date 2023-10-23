import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        var gson = new Gson();
        Cat[] cats;

        var allCats = retrieveCatsData();

        cats = gson.fromJson(allCats, Cat[].class);
//      Получим фильтрованный лист
        List<Cat> catsWithVotes = Arrays.stream(cats).filter(cat -> cat.getUpvotes() > 0)
                .toList();

//      Тут чисто на экран выводим
        var gsonPP = new GsonBuilder().setPrettyPrinting().create();

        String s = gsonPP.toJson(catsWithVotes);

        System.out.println(s);

//      Этот вариант вывода, если квадратные скобочки вначале и конце не нравятся
        for (Cat cat : catsWithVotes) {
            System.out.println(gsonPP.toJson(cat));
        }
    }

    static String retrieveCatsData() throws IOException {

/*
//      Всё решалось намного проще...
        var url = new URL("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
        var urlConnection = url.openConnection();

        var stringInput = new String(urlConnection.getInputStream().readAllBytes());
        System.out.println(stringInput);
*/
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet("https://raw.githubusercontent.com/" +
                "netology-code/jd-homeworks/master/http/task1/cats");

        CloseableHttpResponse response = httpClient.execute(request);

        return (new String(response.getEntity().getContent().readAllBytes()));
    }
}
