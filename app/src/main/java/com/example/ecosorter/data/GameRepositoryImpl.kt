package com.example.ecosorter.data

import android.content.Context
import androidx.core.content.edit
import com.example.ecosorter.R
import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.entity.Level
import com.example.ecosorter.domain.entity.Question
import com.example.ecosorter.domain.entity.TrashCategory
import com.example.ecosorter.domain.entity.TrashItem
import com.example.ecosorter.domain.entity.WrongAnswer
import com.example.ecosorter.domain.repository.GameRepository

class GameRepositoryImpl(private val context: Context) : GameRepository {

    private data class TrashData(
        val category: TrashCategory,
        val imageResId: Int
    )

    override fun getQuestion(): Question {
        val trashItem = trashItemsList.random()
        val options = TrashCategory.entries.toTypedArray().toList()
        return Question(trashItem, options)
    }

    override fun getGameResult(
        level: Level,
        countOfRightAnswers: Int,
        countOfQuestions: Int,
        wrongAnswers: List<WrongAnswer>,
        currentGlobalStreak: Int
    ): GameResult {
        val percentOfRightAnswers = getPercentOfRightAnswers(countOfRightAnswers, countOfQuestions)
        val winner = countOfRightAnswers >= level.minCountOfRightAnswers &&
                percentOfRightAnswers >= level.minPercentOfRightAnswers
        return GameResult(
            winner,
            countOfRightAnswers,
            countOfQuestions,
            percentOfRightAnswers,
            level,
            wrongAnswers,
            currentGlobalStreak
        )
    }

    override fun checkAndSaveHighScore(levelName: String, score: Int): Int {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME,
            Context.MODE_PRIVATE)
        val currentHighScore = sharedPrefs.getInt(levelName, 0)
        val highScore = currentHighScore.coerceAtLeast(score)
        sharedPrefs.edit {
                putInt(levelName, highScore)
            }
        return highScore
    }

    override fun getCurrentGlobalStreak(level: Level): Int {
        val sharedPrefs = context.getSharedPreferences(PREFS_CURRENT_STREAK,
            Context.MODE_PRIVATE)
        return sharedPrefs.getInt(level.name, 0)
    }

    override fun saveCurrentGlobalStreak(level: Level, currentStreak: Int) {
        val sharedPrefs = context.getSharedPreferences(PREFS_CURRENT_STREAK,
            Context.MODE_PRIVATE)
        sharedPrefs.edit {
            putInt(level.name, currentStreak)
        }
    }

    override fun getMaxGlobalStreak(level: Level): Int {
        val sharedPrefs = context.getSharedPreferences(PREFS_MAX_STREAK,
        Context.MODE_PRIVATE)
        return sharedPrefs.getInt(level.name, 0)
    }

    override fun saveMaxGlobalStreak(
        level: Level,
        maxStreak: Int
    ) {
        val sharedPrefs = context.getSharedPreferences(PREFS_MAX_STREAK,
            Context.MODE_PRIVATE)
        sharedPrefs.edit {
            putInt(level.name, maxStreak)
        }
    }

    private fun getPercentOfRightAnswers(countOfRightAnswers: Int, countOfQuestions: Int): Int {
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private companion object {

        const val PREFS_NAME = "eco_sorter_high_scores"
        const val PREFS_CURRENT_STREAK = "eco_sorter_current_streak"
        const val PREFS_MAX_STREAK = "eco_sorter_max_streak"
        val TRASH_MAP = mapOf(
            "Пластиковая бутылка из-под воды" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_water_bottle),
            "Одноразовый пластиковый стаканчик" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_disposable_cup),
            "Полиэтиленовый пакет-майка" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_t_shirt_bag),
            "Флакон от шампуня или геля" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_shampoo_bottle),
            "Крышка от газировки" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_bottle_cap),
            "Пластиковая трубочка для напитков" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_drinking_straw),
            "Контейнер для еды на вынос" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_takeout_food_container),
            "Бутылка от моющего средства" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_detergent_bottle),
            "Упаковка от влажных салфеток" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_wet_wipes_packaging),
            "Пленка-пупырка для посылок" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_bubble_wrap_packaging),
            "Стаканчик от йогурта" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_yogurt_cup),
            "Пластиковая ложка или вилка" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_spoon_and_fork),
            "Пищевая пленка" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_food_wrap),
            "Игрушка из киндер-сюрприза" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_toy_kinder_surprise),
            "Флакон от жидкого мыла" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_liquid_soap_dispenser_bottle),
            "Упаковка от стирального порошка" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_laundry_detergent_jug),
            "Тюбик от зубной пасты" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_toothbrush_tube),
            "Пластиковая вешалка для одежды" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_clothes_hanger),
            "Зубная щетка" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_toothbrush),
            "Блистер от таблеток" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_pill_blister_pack),
            "Пластиковый маркер без стержня" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_marker_pen),
            "Сломанная пластиковая линейка" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_broken_ruler),
            "Подложка от печенья или конфет" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_cookie_tray),
            "Баночка от крема для лица" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_face_cream_jar),
            "Футляр от зубной нити" to TrashData(TrashCategory.PLASTIC, R.drawable.plastic_dental_floss_case),

            "Банановая кожура" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_banana_peel),
            "Огрызок спелого яблока" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_apple_core),
            "Яичная скорлупа" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_eggshell),
            "Использованная чайная заварка" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_used_tea_leaves),
            "Остатки вареных макарон" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_leftover_cooked_pasta),
            "Картофельные очистки" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_potato_peelings),
            "Арбузная корка" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_watermelon_rind),
            "Шелуха от семечек или лука" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_sunflower_seed_hulls_and_onion_skins),
            "Кофейная гуща из турки" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_used_coffee_grounds),
            "Подсохший кусочек хлеба" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_stale_bread_slice),
            "Очистки от моркови" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_carrot_peelings),
            "Косточки от сливы или вишни" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_plum_and_cherry_pits),
            "Куриные кости от обеда" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_leftover_chicken_bones),
            "Увядший букет цветов" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_withered_flower_bouquet),
            "Листья комнатного растения" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_houseplant_leaves),
            "Остатки овсяной каши" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_leftover_oatmeal_porridge),
            "Грибные очистки" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_mushroom_trimmings),
            "Испорченный помидор" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_rotten_tomato),
            "Корка от апельсина" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_orange_peel),
            "Кочан от капусты" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_cabbage_core),
            "Кожа от запеченной курицы" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_roasted_chicken_skin),
            "Рыбные кости и хвост" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_fish_bones_and_tail),
            "Скорлупа от грецких орехов" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_walnut_shells),
            "Листья салата" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_lettuce_leaves),
            "Испорченный сыр" to TrashData(TrashCategory.ORGANIC, R.drawable.organic_spoiled_cheese),

            "Старая газета" to TrashData(TrashCategory.PAPER, R.drawable.paper_old_newspaper),
            "Картонная коробка от обуви" to TrashData(TrashCategory.PAPER, R.drawable.paper_cardboard_shoebox),
            "Школьная тетрадь" to TrashData(TrashCategory.PAPER, R.drawable.paper_school_notebook),
            "Бумажный пакет из супермаркета" to TrashData(TrashCategory.PAPER, R.drawable.paper_grocery_bag),
            "Рекламная листовка или флаер" to TrashData(TrashCategory.PAPER, R.drawable.paper_advertising_flyer),
            "Картонная втулка от полотенец" to TrashData(TrashCategory.PAPER, R.drawable.cardboard_paper_towel_roll),
            "Журнал со страницами" to TrashData(TrashCategory.PAPER, R.drawable.paper_open_magazine_with_pages),
            "Конверт от письма" to TrashData(TrashCategory.PAPER, R.drawable.paper_mail_envelope),
            "Офисная бумага А4 для печати" to TrashData(TrashCategory.PAPER, R.drawable.paper_a4_printer_paper_stack),
            "Картонный стаканчик из-под кофе" to TrashData(TrashCategory.PAPER, R.drawable.paper_coffee_cup),
            "Упаковка от овсяных хлопьев" to TrashData(TrashCategory.PAPER, R.drawable.paper_cereal_cardboard_box),
            "Старая бумажная книга" to TrashData(TrashCategory.PAPER, R.drawable.paper_old_hardcover_book),
            "Календарь-домик" to TrashData(TrashCategory.PAPER, R.drawable.paper_desk_tent_calendar),
            "Бумажный чек из магазина" to TrashData(TrashCategory.PAPER, R.drawable.paper_shopping_receipt),
            "Коробка от зубной пасты" to TrashData(TrashCategory.PAPER, R.drawable.toothpaste_cardboard_box),
            "Яичный лоток из прессованного картона" to TrashData(TrashCategory.PAPER, R.drawable.molded_pulp_egg_carton),
            "Альбом для рисования" to TrashData(TrashCategory.PAPER, R.drawable.paper_sketchbook),
            "Подарочная бумажная коробка" to TrashData(TrashCategory.PAPER, R.drawable.paper_gift_box),
            "Упаковка от замороженной пиццы" to TrashData(TrashCategory.PAPER, R.drawable.paper_frozen_pizza_box),
            "Детская раскраска" to TrashData(TrashCategory.PAPER, R.drawable.paper_children_coloring_book),
            "Бумажный пакет из-под муки" to TrashData(TrashCategory.PAPER, R.drawable.paper_flour_bag),
            "Фантик от конфеты (бумажный)" to TrashData(TrashCategory.PAPER, R.drawable.paper_candy_wrapper),
            "Картонная папка для документов" to TrashData(TrashCategory.PAPER, R.drawable.paper_cardboard_document_folder),
            "Блокнот для записей" to TrashData(TrashCategory.PAPER, R.drawable.paper_notepad),
            "Почтовая открытка" to TrashData(TrashCategory.PAPER, R.drawable.paper_postcard),

            "Стеклянная банка из-под варенья" to TrashData(TrashCategory.GLASS, R.drawable.glass_jam_jar),
            "Бутылка от лимонада или колы" to TrashData(TrashCategory.GLASS, R.drawable.glass_soda_bottle),
            "Осколок разбитого зеркала" to TrashData(TrashCategory.GLASS, R.drawable.glass_broken_mirror_shard),
            "Пустой флакон от духов" to TrashData(TrashCategory.GLASS, R.drawable.glass_empty_perfume_bottle),
            "Аптечный стеклянный пузырек" to TrashData(TrashCategory.GLASS, R.drawable.glass_medicine_vial),
            "Банка из-под детского питания" to TrashData(TrashCategory.GLASS, R.drawable.glass_baby_food_jar),
            "Осколок стакана для воды" to TrashData(TrashCategory.GLASS, R.drawable.glass_broken_water_glass_shard),
            "Бутылка от оливкового масла" to TrashData(TrashCategory.GLASS, R.drawable.glass_olive_oil_bottle),
            "Флакон от лака для ногтей" to TrashData(TrashCategory.GLASS, R.drawable.glass_nail_polish_bottle),
            "Стеклянная банка от майонеза" to TrashData(TrashCategory.GLASS, R.drawable.glass_mayonnaise_jar),
            "Разбитая лампа накаливания" to TrashData(TrashCategory.GLASS, R.drawable.glass_broken_incandescent_light_bulb),
            "Стеклянная пипетка" to TrashData(TrashCategory.GLASS, R.drawable.glass_dropper),
            "Флакон от капель для носа" to TrashData(TrashCategory.GLASS, R.drawable.glass_nasal_drops_bottle),
            "Стеклянный контейнер" to TrashData(TrashCategory.GLASS, R.drawable.glass_food_container),
            "Флакон от тонального cream" to TrashData(TrashCategory.GLASS, R.drawable.glass_foundation_bottle),
            "Бутылка от соуса терияки" to TrashData(TrashCategory.GLASS, R.drawable.glass_teriyaki_sauce_bottle),
            "Осколок оконного стекла" to TrashData(TrashCategory.GLASS, R.drawable.glass_window_shard),
            "Стеклянная баночка от специй" to TrashData(TrashCategory.GLASS, R.drawable.glass_spice_jar),
            "Пустая ампула для лекарств" to TrashData(TrashCategory.GLASS, R.drawable.glass_empty_medical_ampoule),
            "Стеклянная крышка от кастрюли" to TrashData(TrashCategory.GLASS, R.drawable.glass_pot_lid),
            "Флакон от мицеллярной воды" to TrashData(TrashCategory.GLASS, R.drawable.glass_micellar_water_bottle),
            "Стеклянный подсвечник" to TrashData(TrashCategory.GLASS, R.drawable.glass_candle_holder),
            "Осколок стеклянной тарелки" to TrashData(TrashCategory.GLASS, R.drawable.glass_broken_plate_shard),
            "Флакон от сыворотки для лица" to TrashData(TrashCategory.GLASS, R.drawable.glass_face_serum_bottle),
            "Стеклянная банка из-под кофе" to TrashData(TrashCategory.GLASS, R.drawable.glass_coffee_jar)
        )
    }
    val trashItemsList = TRASH_MAP.entries.map { TrashItem(it.key, it.value.category, it.value.imageResId) }

}