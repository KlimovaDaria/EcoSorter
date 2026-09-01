package com.example.ecosorter.data

import android.content.Context
import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.entity.Level
import com.example.ecosorter.domain.entity.Question
import com.example.ecosorter.domain.entity.TrashCategory
import com.example.ecosorter.domain.entity.TrashItem
import com.example.ecosorter.domain.repository.GameRepository

class GameRepositoryImpl(private val context: Context) : GameRepository {
    override fun getQuestion(): Question {
        val trashItem = trashItemsList.random()
        val options = TrashCategory.entries.toTypedArray().toList()
        return Question(trashItem, options)
    }

    override fun getGameResult(
        level: Level,
        countOfRightAnswers: Int,
        countOfQuestions: Int
    ): GameResult {
        val percentOfRightAnswers = getPercentOfRightAnswers(countOfRightAnswers, countOfQuestions)
        val winner = countOfRightAnswers >= level.minCountOfRightAnswers &&
                percentOfRightAnswers >= level.minPercentOfRightAnswers
        return GameResult(
            winner,
            countOfRightAnswers,
            countOfQuestions,
            percentOfRightAnswers,
            level
        )
    }

    override fun getHighScoreForCurrentLevel(levelName: String): Int {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getInt(levelName, 0)
    }

    private fun getPercentOfRightAnswers(countOfRightAnswers: Int, countOfQuestions: Int): Int {
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    companion object {

        const val PREFS_NAME = "eco_sorter_high_scores"
        private val TRASH_MAP = mapOf(
            // === ПЛАСТИК (25 предметов) ===
            "Пластиковая бутылка из-под воды" to TrashCategory.PLASTIC,
            "Одноразовый пластиковый стаканчик" to TrashCategory.PLASTIC,
            "Полиэтиленовый пакет-майка" to TrashCategory.PLASTIC,
            "Флакон от шампуня или геля" to TrashCategory.PLASTIC,
            "Крышка от газировки" to TrashCategory.PLASTIC,
            "Пластиковая трубочка для напитков" to TrashCategory.PLASTIC,
            "Контейнер для еды на вынос" to TrashCategory.PLASTIC,
            "Бутылка от моющего средства" to TrashCategory.PLASTIC,
            "Упаковка от влажных салфеток" to TrashCategory.PLASTIC,
            "Пленка-пупырка для посылок" to TrashCategory.PLASTIC,
            "Стаканчик от йогурта" to TrashCategory.PLASTIC,
            "Пластиковая ложка или вилка" to TrashCategory.PLASTIC,
            "Пищевая пленка" to TrashCategory.PLASTIC,
            "Игрушка из киндер-сюрприза" to TrashCategory.PLASTIC,
            "Флакон от жидкого мыла" to TrashCategory.PLASTIC,
            "Упаковка от стирального порошка" to TrashCategory.PLASTIC,
            "Тюбик от зубной пасты" to TrashCategory.PLASTIC,
            "Пластиковая вешалка для одежды" to TrashCategory.PLASTIC,
            "Зубная щетка" to TrashCategory.PLASTIC,
            "Блистер от таблеток" to TrashCategory.PLASTIC,
            "Пластиковый маркер без стержня" to TrashCategory.PLASTIC,
            "Сломанная пластиковая линейка" to TrashCategory.PLASTIC,
            "Подложка от печенья или конфет" to TrashCategory.PLASTIC,
            "Баночка от крема для лица" to TrashCategory.PLASTIC,
            "Футляр от зубной нити" to TrashCategory.PLASTIC,

            // === БУМАГА (25 предметов) ===
            "Старая газета" to TrashCategory.PAPER,
            "Картонная коробка от обуви" to TrashCategory.PAPER,
            "Школьная тетрадь" to TrashCategory.PAPER,
            "Бумажный пакет из супермаркета" to TrashCategory.PAPER,
            "Рекламная листовка или флаер" to TrashCategory.PAPER,
            "Картонная втулка от полотенец" to TrashCategory.PAPER,
            "Журнал со страницами" to TrashCategory.PAPER,
            "Конверт от письма" to TrashCategory.PAPER,
            "Офисная бумага А4 для печати" to TrashCategory.PAPER,
            "Картонный стаканчик из-под кофе" to TrashCategory.PAPER,
            "Упаковка от овсяных хлопьев" to TrashCategory.PAPER,
            "Старая бумажная книга" to TrashCategory.PAPER,
            "Календарь-домик" to TrashCategory.PAPER,
            "Бумажный чек из магазина" to TrashCategory.PAPER,
            "Коробка от зубной пасты" to TrashCategory.PAPER,
            "Яичный лоток из прессованного картона" to TrashCategory.PAPER,
            "Альбом для рисования" to TrashCategory.PAPER,
            "Подарочная бумажная коробка" to TrashCategory.PAPER,
            "Упаковка от замороженной пиццы" to TrashCategory.PAPER,
            "Детская раскраска" to TrashCategory.PAPER,
            "Бумажный пакет из-под муки" to TrashCategory.PAPER,
            "Фантик от конфеты (бумажный)" to TrashCategory.PAPER,
            "Картонная папка для документов" to TrashCategory.PAPER,
            "Блокнот для записей" to TrashCategory.PAPER,
            "Почтовая открытка" to TrashCategory.PAPER,

            // === ОРГАНИКА (25 предметов) ===
            "Банановая кожура" to TrashCategory.ORGANIC,
            "Огрызок спелого яблока" to TrashCategory.ORGANIC,
            "Яичная скорлупа" to TrashCategory.ORGANIC,
            "Использованная чайная заварка" to TrashCategory.ORGANIC,
            "Остатки вареных макарон" to TrashCategory.ORGANIC,
            "Картофельные очистки" to TrashCategory.ORGANIC,
            "Арбузная корка" to TrashCategory.ORGANIC,
            "Шелуха от семечек или лука" to TrashCategory.ORGANIC,
            "Кофейная гуща из турки" to TrashCategory.ORGANIC,
            "Подсохший кусочек хлеба" to TrashCategory.ORGANIC,
            "Очистки от моркови" to TrashCategory.ORGANIC,
            "Косточки от сливы или вишни" to TrashCategory.ORGANIC,
            "Куриные кости от обеда" to TrashCategory.ORGANIC,
            "Увядший букет цветов" to TrashCategory.ORGANIC,
            "Листья комнатного растения" to TrashCategory.ORGANIC,
            "Остатки овсяной каши" to TrashCategory.ORGANIC,
            "Грибные очистки" to TrashCategory.ORGANIC,
            "Испорченный помидор" to TrashCategory.ORGANIC,
            "Корка от апельсина" to TrashCategory.ORGANIC,
            "Кочан от капусты" to TrashCategory.ORGANIC,
            "Кожа от запеченной курицы" to TrashCategory.ORGANIC,
            "Рыбные кости и хвост" to TrashCategory.ORGANIC,
            "Скорлупа от грецких орехов" to TrashCategory.ORGANIC,
            "Листья салата" to TrashCategory.ORGANIC,
            "Испорченный сыр" to TrashCategory.ORGANIC,

            // === СТЕКЛО (25 предметов) ===
            "Стеклянная банка из-под варенья" to TrashCategory.GLASS,
            "Бутылка от лимонада или колы" to TrashCategory.GLASS,
            "Осколок разбитого зеркала" to TrashCategory.GLASS,
            "Пустой флакон от духов" to TrashCategory.GLASS,
            "Аптечный стеклянный пузырек" to TrashCategory.GLASS,
            "Банка из-под детского питания" to TrashCategory.GLASS,
            "Осколок стакана для воды" to TrashCategory.GLASS,
            "Бутылка от оливкового масла" to TrashCategory.GLASS,
            "Флакон от лака для ногтей" to TrashCategory.GLASS,
            "Стеклянная банка от майонеза" to TrashCategory.GLASS,
            "Разбитая лампа накаливания" to TrashCategory.GLASS,
            "Стеклянная пипетка" to TrashCategory.GLASS,
            "Флакон от капель для носа" to TrashCategory.GLASS,
            "Стеклянный контейнер" to TrashCategory.GLASS,
            "Флакон от тонального крема" to TrashCategory.GLASS,
            "Бутылка от соуса терияки" to TrashCategory.GLASS,
            "Осколок оконного стекла" to TrashCategory.GLASS,
            "Стеклянная баночка от специй" to TrashCategory.GLASS,
            "Пустая ампула для лекарств" to TrashCategory.GLASS,
            "Стеклянная крышка от кастрюли" to TrashCategory.GLASS,
            "Флакон от мицеллярной воды" to TrashCategory.GLASS,
            "Стеклянный подсвечник" to TrashCategory.GLASS,
            "Осколок стеклянной тарелки" to TrashCategory.GLASS,
            "Флакон от сыворотки для лица" to TrashCategory.GLASS,
            "Стеклянная банка из-под кофе" to TrashCategory.GLASS
        )
    }

    private val trashItemsList = TRASH_MAP.entries.map { TrashItem(it.key, it.value) }

}