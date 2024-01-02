package ua.com.honchar.arstudy.presentation

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize
import ua.com.honchar.arstudy.R

@Parcelize
data class Animal(
    val name: String,
    val modelName: String,
    @DrawableRes val icon: Int,
    val info: String,
    val path: String= ""
): Parcelable

fun getAnimals(): List<Animal> = listOf(
    Animal(
        name = "Planet",
        modelName = "system.glb",
        icon = R.drawable.tiger,
        info = "",
        path = "http://192.168.0.103:8080/models/system.glb"
    ),
    Animal(
        name = "Тигр",
        modelName = "http://192.168.0.103:8080/models/tiger.glb",
        icon = R.drawable.tiger,
        info = "Тигр, пантера тигр — великий ссавець родини котових, один із сучасних п'ятьох видів роду пантер (Panthera). Слово «тигр» походить від дав.-гр. τίγρις, яке у свою чергу походить із індоіранських мов, пов'язано з ав. t̰igri — «стріла», натякаючи на стрімкість тигра. Еволюційним центром походження і сучасного ареалу є Східна та Південно-Східна Азія. Бувши найбільшим у світі представником родини котячих, тигр у всіх екосистемах є верхівкою харчової піраміди. Згідно з палеонтологічними даними, розмір сучасних тигрів приблизно дорівнює найбільшим із викопних представників родини.\n" +
                "\n" +
                "Найчисельнішим із підвидів тигра є бенгальський тигр (номінативний підвид), чисельність якого становить близько 80 % загальної кількості виду. Він розповсюджений на території Індії, Бангладеш, М'янми, Бутану та Непалу.\n" +
                "\n" +
                "Тигр — вид, що перебуває під загрозою вимирання. Нині кількість цих тварин у неволі більша, ніж у природі.\n" +
                "\n" +
                "За способом життя тигр є одинаком. Він є видом виразно територіальним, що взагалі характерне для котячих. Звичним для тигра ландшафтом є середньо- та щільнозарослі лісові ділянки, хоча інколи його можна зустріти і на відкритій місцевості.\n" +
                "\n" +
                "Як і в більшості інших котячих, основним способом полювання є підкрадання або вичікування в засідці зі швидким, але коротким фінальним кидком. Більша частина здобичі становить середні та великі копитні, але тигр може полювати і на зовсім дрібних тварин, аж до щурів.\n" +
                "\n" +
                "Самці помітно більші за самиць, і мають більшу територію. Також виразні розмірні розбіжності спостерігаються між дев'ятьма підвидами цього виду, що збереглися до історичних часів."
    ),
    Animal(
        name = "Кінь",
        modelName = "horse.glb",
        icon = R.drawable.horse,
        info = "Голова коня — витягнута, суха, з великими живими очима, широкими ніздрями і великими або середньої величини загостреними і досить рухливими вухами. У домашнього коня вуха помірної величини (набагато менше половини голови), грива довга, звішується, шия довга м'язиста, тулуб округлений, хвіст покритий довгим волоссям від основи; масть надзвичайно різна: чорна, бура, руда, чала, біла, сіра, часто з білими плямами на голові та ногах; як виняток зустрічаються смуги на плечах, спині та ногах. Ноги високі, помірної товщини, стрункі; першого і п'ятого пальця немає зовсім, від 2 і 4 існують лише зачатки (рудименти) у вигляді паличкоподібних кісток п'ястка та плесна (так зв. грифельних), прилеглих до товстої п'ясткової або плеснової кістки сильно розвиненого середнього пальця; копито одягає лише кінець середнього пальця (на них прилягає вся вага тіла); на внутрішній стороні зап'ястя і п'ят знаходяться рогові мозолясті потовщення, шишки (рогові місця знаходяться також позаду місця зчленування пальця з вище розташованими частинами).\n" +
                "\n" +
                "З відчуттів найкраще розвинений слух, потім зір і, нюх. Дикі коні живуть табунами, зазвичай невеликими. Табуни складаються з кількох самиць і самця, переважно в степових місцевостях, відрізняються великою швидкістю і обережністю."
    ),
    Animal(
        name = "Кіт",
        modelName = "kitty.glb",
        icon = R.drawable.cat,
        info = "Кіт — рід хижих ссавців родини котових (Felidae). У деяких старіших системах класифікації до нього зараховували всіх представників малих кішок (Felinae), проте зараз безпосередньо до роду відносяться лише кілька видів невеликих тварин, що мешкають у Євразії та Африці.\n" +
                "\n" +
                "Найвідомішим представником цього роду є свійський кіт — підвид кота лісового.\n" +
                "\n" +
                "За розміром найменшим представником роду є вид мураховий тигр довжина тіла і голови якого становить від 38 до 44 см. Найбільшим є кіт очеретяний із довжиною тіла і голови від 62 до 76 см. Види котів (Felis) мешкають у широкому різновиді природних середовищ, від болотяних до пустель, і харчуються здебільшого малими мишоподібними, птахами й іншими дрібними тваринами.\n" +
                "\n" +
                "Генетичні дослідження показують, що Кіт (Felis), Манул (Otocolobus) і Азійський кіт (Prionailurus) походять від Євразійського прародителя, що існував близько 6,2 мільйонів років тому, і що види Котів розділилися від 3,04 до 0.99 мільйонів років тому."
    ),
    Animal(
        name = "Пес",
        modelName = "dog.glb",
        icon = R.drawable.dog,
        info = "Пес сві́йський або соба́ка сві́йський — культигенна тварина. Термін застосовують як для домашніх, так і для бездомних тварин. Свійський пес був одним з найбільш широко застосовуваних службових та компанійських тварин протягом всієї історії людства.\n" +
                "\n" +
                "За різними оцінками, одомашнення вовка відбулося від 100 000 до 15 000 років тому. Дослідження мітохондріальної ДНК показує, що розділення еволюційних ліній собак і вовків відбулося близько 100 000 років тому. Собака швидко став незамінним у всіх світових культурах та був дуже цінним у ранніх людських поселеннях. Зокрема вважають, що успішна еміграція через Берингову протоку була б неможливою без їздових собак. Собаки виконують багато видів робіт для людей, таких як полювання, охорона, служба в поліції та військах, а також собаки допомагають пасти стада худоби, допомагають особам з інвалідністю та служать компанійськими сімейними собаками. Ця універсальність, більша, ніж практично в будь-якої іншої відомої людству тварини, дала собаці прізвисько «найкращий друг людини». За підрахунками, на планеті на 2015 рік проживало близько 525 млн собак.\n" +
                "\n" +
                "Завдяки селекції було виведено сотні різноманітних порід і нині між собаками різних порід виявляють більше поведінкових та морфологічних відмінностей, ніж у будь-яких інших наземних ссавців. Наприклад, висота в холці може варіювати від кількох сантиметрів (чихуахуа) до майже метра (ірландський вольфгаунд, великий данець); забарвлення — від білого до чорного, включаючи світло-жовте, сіре, коричневе з великим розмаїттям відтінків."
    ),
    Animal(
        name = "О́леневі",
        modelName = "deer.glb",
        icon = R.drawable.deer,
        info = "О́леневі  — родина ссавців з підряду жуйних ряду оленеподібних, що складається з 55 сучасних видів. Оленеві є місцевими в Євразії, Америці й крайній північно-західній Африці, де є лише один підвид місцевих оленів, берберійський благородний олень. Багато представників родини взяті під охорону. Найбільший з оленевих, лось може сягати ваги 800 кг, а найменший вид, північний пуду важить до 9 кг. Зазвичай оленеві мають компактний тулуб і потужні подовжені ноги. Окрім китайського водяного оленя, усі самці оленевих мають розлогі роги, а карибу є єдиним видом, у якого і самці, і самки мають роги. Усі оленеві живляться травою, невеликими кущами та листям.\n" +
                "\n" +
                "У фауні України родина представлена 5 видами — Cervinae: олень благородний, інтродуцент олень японський, інтродуцент лань; Capreolinae: сарна європейська, лось звичайний."
    ),
    Animal(
        name = "Пінгві́нові",
        modelName = "pinguin.glb",
        icon = R.drawable.penguin,
        info = "Пінгві́нові — родина кілегрудих птахів. Родина пінгвінові є єдиною групою ряду пінгвіноподібні. Пінгвіни є досить відокремленою групою птахів, що має давнє походження. Ряд налічує 6 родів і 16 видів, які утворюють одну родину. Окрім того, відомо 36 викопних видів. Найстародавніші рештки пінгвінів знайдені в Новій Зеландії (нижній міоцен). Найдавнішим видом був пінгвін Kairuku waitaki."
    )
)