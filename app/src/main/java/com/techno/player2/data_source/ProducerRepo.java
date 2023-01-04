package com.techno.player2.data_source;

import com.techno.player2.models.Producer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProducerRepo {
    String sectionName;

    public ProducerRepo(String sectionName){
        this.sectionName=sectionName;
    };
    public List<Producer> cartoonCategories() {
        return Arrays.asList(
                newbie(),disney(), netflix(),categoryFamily(),
                categoryHorror(),
                categoryAction(),
                categoryComedy(),
                categoryDocumentary(),
                categoryThriller(),
                categoryAdventure(),
                categoryDetective(),
                categoryCriminal(),
                categoryBiography(),
                categoryArtHouse(),
                categoryMilitary(),
                categoryHistorical(),
                categorySport(),
                categoryWestern(),
                categoryForeign(),
                categoryMelodrama(),
                categoryFantasy(),
                categoryRussian(),
                categoryMusical(),
                others()
        );
    }
    public  List<Producer> categories() {

        return Arrays.asList(
                newbie(),
                amazon(),
                appleTV(),
                netflix(),
                hbo(),
                disney(),
                categoryFamily(),
                categoryHorror(),
                categoryAction(),
                categoryComedy(),
                categoryDocumentary(),
                categoryThriller(),
                categoryAdventure(),
                categoryDetective(),
                categoryCriminal(),
                categoryBiography(),
                categoryArtHouse(),
                categoryMilitary(),
                categoryHistorical(),
                categorySport(),
                categoryWestern(),
                categoryForeign(),
                categoryMelodrama(),
                categoryFantasy(),
                categoryRussian(),
                categoryMusical(),
                others()

                );
    }
    Producer categoryMusical() {
        return new Producer(
                "Мюзиклы",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/musical"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/musical"
                        : "cartoons/musical"));
    } Producer categoryRussian() {
        return new Producer(
                "Русские",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/russian"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/russian"
                        : "cartoons/russian"));
    }
    Producer categoryFantasy() {
        return new Producer(
                "Фэнтези",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/fantasy"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/fantasy"
                        : "cartoons/fantasy"));
    }
    Producer categoryMelodrama() {
        return new Producer(
                "Мелодрамы",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/melodrama"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/melodrama"
                        : "cartoons/melodrama"));
    }
    Producer categoryForeign() {
        return new Producer(
                "Зарубежные",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/foreign"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/foreign"
                        : "cartoons/foreign"));
    }
    Producer categoryWestern() {
        return new Producer(
                "Вестерны",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/western"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/western"
                        : "cartoons/western"));
    }
    Producer categorySport() {
        return new Producer(
                "Спортивные",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/sport"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/sport"
                        : "cartoons/sport"));
    }Producer categoryHistorical() {
        return new Producer(
                "Исторические",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/historical"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/historical"
                        : "cartoons/historical"));
    }
    Producer categoryMilitary() {
        return new Producer(
                "Военные",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/military"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/military"
                        : "cartoons/military"));
    }
    Producer categoryArtHouse() {
        return new Producer(
                "Арт-Хаус",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/arthouse"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/arthouse"
                        : "cartoons/arthouse"));
    }
    Producer categoryBiography() {
        return new Producer(
                "Криминал",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/biographical"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/biographical"
                        : "cartoons/biographical"));
    }
    Producer categoryCriminal() {
        return new Producer(
                "Криминал",
                Objects.equals(sectionName, "Премьеры")
                        ? "films/crime"
                        : (Objects.equals(sectionName, "Сериалы")
                        ? "series/crime"
                        : "cartoons/crime"));
    }
    Producer categoryDocumentary() {
        return new Producer(
                "Документальные",
                Objects.equals(sectionName, "Премьеры")
                ? "films/documentary"
                : (Objects.equals(sectionName, "Сериалы")
                ? "series/documentary"
                : "cartoons/documentary"));
    }
    Producer categoryAction() {
        return new Producer(
                "Боевики",
                Objects.equals(sectionName, "Премьеры")
                ? "films/action"
                : (Objects.equals(sectionName, "Сериалы") ? "series/action" : "cartoons/action"));
    }
    Producer categoryHorror() {
        return new Producer(
                "Ужасы",
                Objects.equals(sectionName, "Премьеры")
                ? "films/horror"
                : (Objects.equals(sectionName, "Сериалы") ? "series/horror" : "cartoons/horror"));
    }
    Producer categoryThriller() {
        return new Producer(
                "Триллеры",
                Objects.equals(sectionName, "Премьеры")
                ? "films/thriller"
                : (Objects.equals(sectionName, "Сериалы")
                ? "series/thriller"
                : "cartoons/thriller"));
    }
    Producer appleTV() {
        return new Producer(
                "Apple TV",
                Objects.equals(sectionName, "Премьеры")
                ? "collections/1416-filmy-apple-tv"
                : "collections/1146-serialy-apple-tv");
    }

    Producer amazon() {
        return new Producer(
                "Амазон",
                Objects.equals(sectionName, "Премьеры")
                ? "collections/1417-filmy-amazon"
                : "collections/831-serialy-amazon");
    }

    Producer hbo() {
        return new Producer(
                "HBO",
                Objects.equals(sectionName, "Премьеры")
                ? "collections/1419-filmy-hbo"
                : "collections/639-serialy-hbo");
    }

    Producer disney() {
        return new Producer(
                "Дисней",
                Objects.equals(sectionName, "Премьеры")
                ? "collections/1075-filmy-disney"
                : (Objects.equals(sectionName, "Сериалы")
                ? "1148-serialy-disney"
                : "collections/1-multfilmy-disney"));
    }

    Producer netflix() {
        return new Producer(
                "Нетфликс",
                Objects.equals(sectionName, "Премьеры")
                ? "collections/834-filmy-netflix"
                : (Objects.equals(sectionName, "Сериалы")
                ? "collections/640-serialy-netflix"
                : "collections/1004-animacionnye-serialy-netflix"));
    }

    Producer newbie() {
        return new Producer(
                "Новинки",
                Objects.equals(sectionName, "Премьеры")
                ? "films/best/2022"
                : (Objects.equals(sectionName, "Сериалы")
                ? "series/best/2022"
                : "cartoons/best/2022"));
    }

    Producer others() {
        return new Producer(
                "Другие",
                Objects.equals(sectionName, "Премьеры")
                ? "films"
                : (Objects.equals(sectionName, "Сериалы") ? "series" : "cartoons"));
    }
    Producer categoryAdventure() {
        return new  Producer(
                 "Приключения",
                Objects.equals(sectionName, "Премьеры")
                ? "films/adventures"
                : (Objects.equals(sectionName, "Сериалы")
                ? "series/adventures"
                : "cartoons/adventures"));
    }

    Producer categoryFamily() {
        return new Producer(
                 "Семейные",
                Objects.equals(sectionName, "Премьеры")
                ? "films/family"
                : (Objects.equals(sectionName, "Сериалы") ? "series/family" : "cartoons/family"));
    }

    Producer categoryDetective() {
        return new Producer(
               "Детективы",
                Objects.equals(sectionName, "Премьеры")
                ? "films/detective"
                : (Objects.equals(sectionName, "Сериалы")
                ? "series/detective"
                : "cartoons/detective"));
    }

    Producer categoryComedy() {
        return new Producer(
                "Комедии",
                Objects.equals(sectionName, "Премьеры")
                ? "films/comedy"
                : (Objects.equals(sectionName, "Сериалы") ? "series/comedy" : "cartoons/comedy"));
    }
}
