package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloWorldController {

    @GetMapping("/")
    public String helloWorld(@RequestParam(value = "message", required = false) String message, Model model) {
        String url = "http://gvs.spb.sudrf.ru/";

        try {
            // Загрузка страницы
            Document doc = Jsoup.connect(url).get();

            // Извлечение заголовка страницы
            String pageTitle = doc.title();
            model.addAttribute("pageTitle", pageTitle);

            // Извлечение текста ключевых слов
            Element keywordsElement = doc.selectFirst("meta[name=Keywords]");
            String keywords = keywordsElement != null ? keywordsElement.attr("content") : "";
            model.addAttribute("keywords", keywords);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "index";
    }

    @PostMapping("/search")
    @ResponseBody
    public List<String> search(@RequestParam("date") String date) {
        String baseUrl = "https://gvs--spb.sudrf.ru/";
        String searchUrl = baseUrl + "modules.php?name=sud_delo&srv_num=1&H_date=" + date;
        List<String> caseUrls = new ArrayList<>();

        try {
            // Отправка GET-запроса для получения страницы с результатами поиска
            Document doc = Jsoup.connect(searchUrl).get();

            // Извлечение всех ссылок на дела
            Elements caseLinks = doc.select("a[href*=/modules.php?name=sud_delo&srv_num=1&name_op=case&]");

            // Добавление ссылок в список
            for (Element link : caseLinks) {
                String caseUrl = baseUrl + link.attr("href");
                caseUrls.add(caseUrl);
            }

            // Вывод информации о количестве найденных ссылок и самих ссылках
            System.out.println("Number of case URLs found: " + caseUrls.size());
            System.out.println("Case URLs: " + caseUrls);
            System.out.println("Search URL: " + searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return caseUrls;
    }

}


