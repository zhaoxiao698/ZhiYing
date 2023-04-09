package com.zhaoxiao.zhiying.data;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.test.CarefulM;
import com.zhaoxiao.zhiying.entity.test.CarefulQuestion;
import com.zhaoxiao.zhiying.entity.test.MatchM;
import com.zhaoxiao.zhiying.entity.test.MatchQuestion;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.entity.test.QuestionType;
import com.zhaoxiao.zhiying.entity.test.TestFtype;
import com.zhaoxiao.zhiying.entity.test.TestStype;

import java.util.ArrayList;
import java.util.List;

import static com.xuexiang.xutil.resource.ResUtils.getString;

public class DataSource {

    public static List<QuestionType> getQuestionType(){
        List<QuestionType> files = new ArrayList<>();
        files.add(new QuestionType("aa","1",""));
        files.add(new QuestionType("bb","2","1"));
        files.add(new QuestionType("cc","3","1"));
        files.add(new QuestionType("dd","4","2"));
        files.add(new QuestionType("ff","5","2"));
        files.add(new QuestionType("gg","6","1"));
        files.add(new QuestionType("hh","7",""));
        files.add(new QuestionType("jj","8",""));
        files.add(new QuestionType("qq","9","3"));
        files.add(new QuestionType("ww","10","9"));
        files.add(new QuestionType("jj","11","3"));
        files.add(new QuestionType("jj","12","4"));
        files.add(new QuestionType("jj","13","4"));
        files.add(new QuestionType("jj","14","4"));
        files.add(new QuestionType("jj","15","4"));
        files.add(new QuestionType("jj","16","7"));
        files.add(new QuestionType("jj","17","7"));
        files.add(new QuestionType("jj","18","7"));

        return files;
    }

    public static List<TestFtype> getTestFtype(){
        List<TestFtype> ftypes = new ArrayList<>();

        List<TestStype> stypes1 = new ArrayList<>();
        stypes1.add(new TestStype(1,"短篇新闻",1000,500,400));
        stypes1.add(new TestStype(2,"长对话",1000,500,400));
        stypes1.add(new TestStype(3,"听力篇章",1000,500,400));
        stypes1.add(new TestStype(4,"讲话/报道/讲座",1000,500,400));
        TestFtype ftype1 = new TestFtype(1,"听力",stypes1,false,1000,500,400);

        List<TestStype> stypes2 = new ArrayList<>();
        stypes2.add(new TestStype(5,"选词填空",1000,500,400));
        stypes2.add(new TestStype(6,"匹配",1000,500,400));
        stypes2.add(new TestStype(7,"仔细阅读",1000,500,400));
        TestFtype ftype2 = new TestFtype(2,"阅读理解",stypes2,false,1000,500,400);

        List<TestStype> stypes3 = new ArrayList<>();
        stypes3.add(new TestStype(8,"汉译英",1000,500,400));
        TestFtype ftype3 = new TestFtype(3,"翻译",stypes3,false,1000,500,400);

        List<TestStype> stypes4 = new ArrayList<>();
        stypes4.add(new TestStype(9,"短文写作",1000,500,400));
        TestFtype ftype4 = new TestFtype(3,"写作",stypes4,false,1000,500,400);

        ftypes.add(ftype1);
        ftypes.add(ftype2);
        ftypes.add(ftype3);
        ftypes.add(ftype4);

        return ftypes;
    }

    public static List<QuestionM> getQuestionList(){
        List<QuestionM> questionList = new ArrayList<>();
        List<CarefulQuestion> carefulQuestions1 = new ArrayList<>();
        carefulQuestions1.add(new CarefulQuestion(51,1,
                "Why do people in many cultures prize the egg?",
                "It is their major source of protein in winter.",
                "It is a welcome sign of the coming of spring.",
                "It can easily be made into a work of art.",
                "It can bring wealth and honor to them.",
                "A"));
        carefulQuestions1.add(new CarefulQuestion(52,1,
                "What do we learn about the decorated “eggs” in Russia?",
                "They are shaped like jewel cases.",
                "They are cherished by the rich.",
                "They are heavily painted in red.",
                "They are favored as a form of art.",
                "D"));
        carefulQuestions1.add(new CarefulQuestion(53,1,
                "Why have contemporary artists continued the egg art tradition?",
                "Eggs serve as an enduring symbol ofnew life.",
                "Eggs have an oval shape appealing to artists.",
                "Eggs reflect the anxieties of people today.",
                "Eggs provide a unique surface to paint on.",
                "A"));
        carefulQuestions1.add(new CarefulQuestion(54,1,
                "Why does Chast enjoy the process of decorating eggs?",
                "She never knows if the egg will break before the design is completed.",
                "She can add multiple details to the design to communicate her idea.",
                "She always derives great pleasure from designing something new.",
                "She is never sure what the final design will look like until the end.",
                "D"));
        carefulQuestions1.add(new CarefulQuestion(55,1,
                "What do we learn from the passage about egg-painting?",
                "It originated in the eastern part of Europe.",
                "It has a history of over two thousand years.",
                "It is the most time-honored form of fancy art.",
                "It is especially favored as a church decoration.",
                "D"));
        String info1 = "<p style=\"text-indent: 2em; line-height: 1;\"><em><strong>Questions</strong></em><strong> 51 to 55 are based on the following passage.</strong></p><p style=\"text-indent: 2em; line-height: 1;\"><u>In spring</u>, <s>chickens</s> start laying again, bringing a welcome source of protein at winter's end. So it's no surprise that cultures around the world celebrate spring by honoring the egg.</p><p style=\"text-indent: 2em;\">Some traditions are simple, like the red eggs that get baked into Greek Easter breads. Others elevate the egg into a fancy art, like the heavily jewel-covered \"eggs\" that were favored by the Russians startingin the 19th century.</p><p style=\"text-indent: 2em;\">的One ancient form of egg art comes to us from Ukraine. For centuries, Ukrainians have been drawing complicated patterns on eggs. Contemporary artists have followed this tradition to create eggs that speak to the anxieties of our age: Life is precious, and delicate. Eggs are, too.</p><p style=\"text-indent: 2em;\">都\"There's something about their delicate nature that appeals to me,\" says New Yorker cartoonist Roz Chast. Several years ago, she became interested in eggs and learned the traditional Ukrainian technique to draw her very modem characters. \"I've broken eggs at every stage of the process—from the very beginning to the very, very end.\"</p><p style=\"text-indent: 2em;\">But there's an appeal in that vulnerability. \"There's part of this sickening horror of knowing you're walking on the edge with this, that I kind of like, knowing that it could all fall apart at any second.\" Chast’s designs, such as a worried man alone in a tiny rowboat, reflect that delicateness.</p><p style=\"text-indent: 2em;\">Traditional Ukrainian decorated eggs also spoke to those fears. The elaborate patterns were believed to offer protection against evil.</p><p style=\"text-indent: 2em;\">\"There's an ancient legend that as long as these eggs are made, evil will not prevail in the world,\" says Joan Brander, a Canadian egg-painter who has been painting eggs for over 60 years, having learned the art from her Ukrainian relatives.</p><p style=\"text-indent: 2em;\">The tradition, dating back to 300 B.C., was later incorporated into the Christian church. The old symbols, however, still endure. A decorated egg with a bird on it, given to a young married couple, is a wish for children. A decorated egg thrown into the field would be a wish for a good harvest.</p>";
        questionList.add(new CarefulM(1,info1,5,carefulQuestions1));

        List<CarefulQuestion> carefulQuestions2 = new ArrayList<>();
        carefulQuestions2.add(new CarefulQuestion(51,2,
                "Why do people in many cultures prize the egg?",
                "It is their major source of protein in winter.",
                "It is a welcome sign of the coming of spring.",
                "It can easily be made into a work of art.",
                "It can bring wealth and honor to them.",
                "A"));
        carefulQuestions2.add(new CarefulQuestion(52,2,
                "What do we learn about the decorated “eggs” in Russia?",
                "They are shaped like jewel cases.",
                "They are cherished by the rich.",
                "They are heavily painted in red.",
                "They are favored as a form of art.",
                "D"));
        carefulQuestions2.add(new CarefulQuestion(53,2,
                "Why have contemporary artists continued the egg art tradition?",
                "Eggs serve as an enduring symbol ofnew life.",
                "Eggs have an oval shape appealing to artists.",
                "Eggs reflect the anxieties of people today.",
                "Eggs provide a unique surface to paint on.",
                "A"));
        carefulQuestions2.add(new CarefulQuestion(54,2,
                "Why does Chast enjoy the process of decorating eggs?",
                "She never knows if the egg will break before the design is completed.",
                "She can add multiple details to the design to communicate her idea.",
                "She always derives great pleasure from designing something new.",
                "She is never sure what the final design will look like until the end.",
                "D"));
        carefulQuestions2.add(new CarefulQuestion(55,2,
                "What do we learn from the passage about egg-painting?",
                "It originated in the eastern part of Europe.",
                "It has a history of over two thousand years.",
                "It is the most time-honored form of fancy art.",
                "It is especially favored as a church decoration.",
                "D"));
        String info2 = "<p style=\"text-indent: 2em; line-height: 1;\"><em><strong>Questions</strong></em><strong> 51 to 55 are based on the following passage.</strong></p><p style=\"text-indent: 2em; line-height: 1;\"><u>In spring</u>, <s>chickens</s> start laying again, bringing a welcome source of protein at winter's end. So it's no surprise that cultures around the world celebrate spring by honoring the egg.</p><p style=\"text-indent: 2em;\">Some traditions are simple, like the red eggs that get baked into Greek Easter breads. Others elevate the egg into a fancy art, like the heavily jewel-covered \"eggs\" that were favored by the Russians startingin the 19th century.</p><p style=\"text-indent: 2em;\">的One ancient form of egg art comes to us from Ukraine. For centuries, Ukrainians have been drawing complicated patterns on eggs. Contemporary artists have followed this tradition to create eggs that speak to the anxieties of our age: Life is precious, and delicate. Eggs are, too.</p><p style=\"text-indent: 2em;\">都\"There's something about their delicate nature that appeals to me,\" says New Yorker cartoonist Roz Chast. Several years ago, she became interested in eggs and learned the traditional Ukrainian technique to draw her very modem characters. \"I've broken eggs at every stage of the process—from the very beginning to the very, very end.\"</p><p style=\"text-indent: 2em;\">But there's an appeal in that vulnerability. \"There's part of this sickening horror of knowing you're walking on the edge with this, that I kind of like, knowing that it could all fall apart at any second.\" Chast’s designs, such as a worried man alone in a tiny rowboat, reflect that delicateness.</p><p style=\"text-indent: 2em;\">Traditional Ukrainian decorated eggs also spoke to those fears. The elaborate patterns were believed to offer protection against evil.</p><p style=\"text-indent: 2em;\">\"There's an ancient legend that as long as these eggs are made, evil will not prevail in the world,\" says Joan Brander, a Canadian egg-painter who has been painting eggs for over 60 years, having learned the art from her Ukrainian relatives.</p><p style=\"text-indent: 2em;\">The tradition, dating back to 300 B.C., was later incorporated into the Christian church. The old symbols, however, still endure. A decorated egg with a bird on it, given to a young married couple, is a wish for children. A decorated egg thrown into the field would be a wish for a good harvest.</p>";
        questionList.add(new CarefulM(2,info2,5,carefulQuestions2));

        List<MatchQuestion> matchQuestions3 = new ArrayList<>();
        matchQuestions3.add(new MatchQuestion(36,1,"People with HSAM have the same memory as ordinary people when it comes to impersonal information.","H"));
        matchQuestions3.add(new MatchQuestion(37,1,"Fantasy proneness will not necessarily cause people to develop HSAM.","K"));
        matchQuestions3.add(new MatchQuestion(38,1,"Veiseh began to remember the details of his everyday experiences after he met his first young love.","C"));
        matchQuestions3.add(new MatchQuestion(39,1,"Many more people with HSAM started to contact researchers due to the mass media.","G"));
        matchQuestions3.add(new MatchQuestion(40,1,"People with HSAM often have to make efforts to avoid focusing on the past.","P"));
        matchQuestions3.add(new MatchQuestion(41,1,"Most people do not have clear memories of past events.","A"));
        matchQuestions3.add(new MatchQuestion(42,1,"HSAM can be both a curse and a blessing.","L"));
        matchQuestions3.add(new MatchQuestion(43,1,"A young woman sought explanation from a brain scientist when she noticed her unusual memory.","E"));
        matchQuestions3.add(new MatchQuestion(44,1,"Some people with HSAM find it very hard to get rid of unpleasant memories.","O"));
        matchQuestions3.add(new MatchQuestion(45,1,"A recent study of people with HSAM reveals that they are liable to fantasy and full absorption in an activity.","I"));
        String info3 = "<p style=\"text-align: center;\"><strong>The Blessing and Curse of the People Who Never Forget</strong></p><p>A handful of people can recall almost every day of their lives in enormous detail—and after years of research, neuroscientists (神经科学专家) are finally beginning to understand how they do it.</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[A] For most of us, memory is a mess of blurred and faded pictures of our lives. As much as we would like to cling on to our past, even the saddest moments can be washed away with time.</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[B] Ask Nima Veiseh what he was doing for any day in the past 15 years, however, and he will give you the details of the weather, what he was wearing, or even what side of the train he was sitting on his journey to work. “My memory is like a library of video tapes, walk-throughs of every day of my life from waking to sleeping,” he explains.</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[C] Veiseh can even put a date on when those tapes started recording: 15 December 2000, when he met his first girlfriend at his best friend's 16th birthday party. He had always had a good memory, but the thrill of young love seems to have shifted a gear in his mind: from now on, he would start recording his whole life in detail. “I could tell you everything about every day after that.”</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[D] Needless to say, people like Veiseh are of great interest to neuroscientists hoping to understand the way the brain records our lives. A couple of recent papers have finally opened a window on these people’s extraordinary minds. And such research might even suggest ways for us all to relive our past with greater clarity.</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[E] “Highly superior autobiographical memory”（or HSAM for short) first came to light in the early 2000s, with a young woman named Jill Price. Emailing the neuroscientist and memory researcher Jim McGaugh one day, she claimed that she could recall every day of her life since the age of 12. Could he help explain her experiences?</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[F] McGaugh invited her to his lab, and began to test her: he would give her a date and ask her to tell him about the world events on that day. True to her word, she was correct almost every time.</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[G] It didn’t take long for magazines and documentary film-makers to come to understand her “total recall”， and thank to the subsequent media interest, a few dozen other subjects (including Veiseh) have since come forward and contacted the team at the University of California, Irvine. [H] Interestingly, their memories are highly self-centred: although they can remember “autobiographical” life events in extraordinary detail, they seem to be no better than average at recalling impersonal information, such as random (任意选取的）lists of words. Nor are they necessarily better at remembering a round of drinks, say. And although their memories are vast, they are still likely to suffer from “false memories”.Clearly, there is no such thing as a “perfect” memory—their extraordinary minds are still using the same flawed tools that the rest of us rely on. The question is, how?</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[I] Lawrence Patihis at the University of Southern Mississippi recently studied around 20 people with HSAM and found that they scored particularly high on two measures: fantasy proneness (倾向)and absorption. Fantasy proneness could be considered a tendency to imagine and daydream, whereas absorption is the tendency to allow your mind to become fully absorbed in an activity to pay complete attention to the sensations (感受)and the experiences. “I’m extremely sensitive to sounds, smells and visual detail,” explains Nicole Donohue, who has taken part in many of these studies. “I definitely feel things more strongly than the average person.”</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[J] The absorption helps them to establish strong foundations for recollection, says Patihis, and the fantasy proneness means that they revisit those memories again and again in the coming weeks and months. Each time this initial memory trace is “replayed”, it becomes even stronger. In some ways, you probably go through that process after a big event like your wedding day,but the difference is that thanks to their other psychological tendencies, the HSAM subjects are doing it day in, day out, for the whole of their lives.</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[K] Not everyone with a tendency to fantasise will develop HSAM, though, so Patihis suggests that something must have caused them to think so much about their past. “Maybe some experience in their childhood meant that they became obsessed (着迷）with calendars and what happened to them,” says Patihis.</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[L] The people with HSAM I’ve interviewed would certainly agree that it can be a mixed blessing. On the plus side, it allows you to relive the most transformative and enriching experiences. Veiseh, for instance, travelled a lot in his youth. In his spare time, he visited the local art galleries, and the paintings are now lodged deep in his autobiographical memories.</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[M] “Imagine being able to remember every painting, on every wall, in every gallery space, between nearly 40 countries ， ” he says. “That’s a big education in art by itself.” With this comprehensive knowledge of the history of art, he has since become a professional painter.</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[N] Donohue, now a history teacher, agrees that it helped during certain parts of her education. “I can definitely remember what I learned on certain days at school. I could imagine what the teacher was saying or what it looked like in the book.”</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[O] Not everyone with HSAM has experienced these benefits, however. Viewing the past in high definition can make it very difficult to get over pain and regret. “It can be very hard to forget embarrassing moments,” says Donohue. “You feel the same emotions—it is just as raw, just as fresh... You can’t turn off that stream of memories, no matter how hard you try.” Veiseh agrees. “It is like having these open wounds—they are just a part of you,” he says.</p><p> &nbsp; &nbsp; &nbsp; &nbsp;[P] This means they often have to make a special effort to lay the past to rest. Bill, for instance, often gets painful “flashbacks”，in which unwanted memories intrude into his consciousness, but overall he has chosen to see it as the best way of avoiding repeating the same mistakes. “Some people are absorbed in the past but not open to new memories, but that’s not the case for me. I look forward to each day and experiencing something new.”</p>";
        questionList.add(new MatchM(1,info3,15,10,matchQuestions3));

        return questionList;
    }
}
