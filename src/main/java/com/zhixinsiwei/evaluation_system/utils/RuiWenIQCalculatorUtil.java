package com.zhixinsiwei.evaluation_system.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhixinsiwei.evaluation_system.common.dto.IQLevel;
import com.zhixinsiwei.evaluation_system.common.dto.IQResult;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName RuiWenIQCalculatorUtil.java
 * @Description TODO
 * @createTime 2026年01月06日 23:04:00
 */
public class RuiWenIQCalculatorUtil {

    // ===== 年龄列定义（5 - 35 共 31 项）=====
    static final int MIN_AGE = 5;
    static final int MAX_AGE = 35;

    // ===== 所有 IQ 档位静态数据（按你表格内容填充）=====
    static final List<IQLevel> IQ_TABLE = Arrays.asList(
            new IQLevel(
                    "130以上", 130, "极高", 99,
                    new int[]{19, 22, 30, 36, 38, 49, 49, 62, 62, 66, 66, 69, 69, 69, 69, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 70, 70, 70, 70, 70, 69, 69},
                    "您的智力在同龄人中属于天才水平，这可能和您答题的状态有关。",
                    "<p>您的智商水平极高</p><p>您的智商水平超过98%同龄人。</p><p>您具有超出常人的知觉组织能力和逻辑推理能力，善于观察、对比和分析，思维情绪有条理，能极快发现事物间的联系，善于利用各种信息解决问题，并能创造性的发现更加行之有效的问题解决方案。</p>"
            ),
            new IQLevel(
                    "120-129", 120, "超高", 91,
                    new int[]{17, 20, 27, 33, 35, 45, 45, 57, 57, 61, 61, 65, 65, 65, 65, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 65, 65, 65, 65, 65, 65, 65},
                    "您的智力在同龄人中属于高智商水平，这可能和您答题的状态有关。",
                    "<p>您的智商水平超高</p><p>您的智商水平远远高出一般同龄人。</p><p>您具有高于常人的知觉组织能力和逻辑推理能力，善于观察、对比和分析，思维情绪有条理，能快速发现事物间的联系，善于利用各种信息解决问题，且多数时候能创造性的解决问题。</p>"
            ),
            new IQLevel(
                    "110-119", 110, "超常", 75,
                    new int[]{15, 18, 24, 30, 32, 41, 41, 52, 52, 56, 56, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60},
                    "您的智力在同龄人中属于超常水平，这可能和您答题的状态有关。",
                    "<p>您的智商水平超常</p><p>您的智商水平超出一般同龄人。</p><p>您具有较好知觉组织能力和逻辑推理能力，善于观察、对比和分析，思维情绪有条理，能发现事物间的联系，善于利用各种信息解决问题。</p>"
            ),
            new IQLevel(
                    "90-109", 90, "正常", 50,
                    new int[]{12, 14, 18, 24, 26, 33, 33, 42, 42, 46, 46, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49},
                    "您的智力在同龄人中属于正常水平，这可能和您答题的状态有关。",
                    "<p>您的智商水平正常</p><p>您的智商已超过50%同龄人。</p><p>您的思维反应较为敏捷，有较强的知觉组织能力，能够主动把握事物间的逻辑联系；发展和学习速度比50%同龄人要快。</p><p>人的智力并非一生不变，学校、家庭都会对个人发展起到较大影响，经过特殊的教育培训，智力水平会得到一定程度的提高和发展。</p>"
            ),
            new IQLevel(
                    "80-89", 80, "中等", 25,
                    new int[]{10, 12, 16, 21, 23, 29, 29, 37, 37, 41, 41, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44},
                    "您的智力在同龄人中属于中等水平，这可能和您答题的状态有关。",
                    "<p>您的智商水平中等</p><p>智商略低于一般同龄人。</p><p>您的思维反应有时不够敏捷，知觉组织能力稍有欠缺，能够把握事物间的逻辑联系；发展和学习速度比同龄人略慢一些。</p><p>人的智力并非一生不变，学校、家庭都会对个人发展起到较大影响，经过特殊的教育培训，智力水平会得到一定程度的提高和发展。</p>"
            ),
            new IQLevel(
                    "70-79", 70, "偏低", 9,
                    new int[]{8, 10, 14, 18, 20, 25, 25, 32, 32, 36, 36, 38, 38, 38, 38, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 38},
                    "您的智力在同龄人中属于偏低水平，这可能和您答题的状态有关。",
                    "<p>您的智商水平偏低</p><p>智商低于一般同龄人。</p><p>具体表现为思维不够清晰，知觉组织能力不足，把握事物间的逻辑联系能力较差；发展和学习速度比同龄人缓慢一些。</p><p>人的智力并非一生不变，学校、家庭都会对个人发展起到较大影响，经过特殊的教育培训，智力水平会得到一定程度的提高和发展。</p>"
            ),
            new IQLevel(
                    "70以下", 60, "低下", 3,
                    new int[]{6, 8, 12, 15, 17, 21, 21, 27, 27, 31, 31, 32, 32, 32, 32, 35, 35, 35, 35, 35, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 32, 32},
                    "您的智力在同龄人中属于较低水平，这可能和您答题的状态有关。",
                    "<p>您的智商水平低下</p><p>远低于一般同龄人，可能存在某种缺陷。</p><p>具体表现为思维混乱，知觉组织能力较差，无法把握事物间的内部联系；发展和学习速度比同龄人缓慢很多，可能需要进入特殊培训学习接受培训。</p><p>人的智力并非一生不变，学校、家庭都会对个人发展起到较大影响，经过特殊的教育培训，智力水平会得到一定程度的提高和发展。</p>"
            )
    );

    // 类别与题目区间
    private static final Map<String, int[]> CATEGORY_RANGES = new LinkedHashMap<>();

    static {
        CATEGORY_RANGES.put("直觉辨识能力", new int[]{1, 12});
        CATEGORY_RANGES.put("类比想象能力", new int[]{13, 24});
        CATEGORY_RANGES.put("类同比较能力", new int[]{25, 36});
        CATEGORY_RANGES.put("比较推理能力", new int[]{37, 48});
        CATEGORY_RANGES.put("系列关系能力", new int[]{49, 60});
        CATEGORY_RANGES.put("抽象推理能力", new int[]{61, 72});
    }

    /**
     * 瑞文标准推理测验 - 正确答案表
     * 来源：官方 6 维能力划分表（每维 12 题）
     * 选项值：A–H
     */
    private static final Map<Integer, String> CORRECT_ANSWER_MAP = new LinkedHashMap<>();

    static {
        // ================= A 直觉辨识能力（1–12） =================
        CORRECT_ANSWER_MAP.put(1, "D");
        CORRECT_ANSWER_MAP.put(2, "E");
        CORRECT_ANSWER_MAP.put(3, "A");
        CORRECT_ANSWER_MAP.put(4, "B");
        CORRECT_ANSWER_MAP.put(5, "F");
        CORRECT_ANSWER_MAP.put(6, "C");
        CORRECT_ANSWER_MAP.put(7, "F");
        CORRECT_ANSWER_MAP.put(8, "A");
        CORRECT_ANSWER_MAP.put(9, "A");
        CORRECT_ANSWER_MAP.put(10, "C");
        CORRECT_ANSWER_MAP.put(11, "D");
        CORRECT_ANSWER_MAP.put(12, "E");

        // ================= AB 类比想象能力（13–24） =================
        CORRECT_ANSWER_MAP.put(13, "D");
        CORRECT_ANSWER_MAP.put(14, "E");
        CORRECT_ANSWER_MAP.put(15, "A");
        CORRECT_ANSWER_MAP.put(16, "F");
        CORRECT_ANSWER_MAP.put(17, "B");
        CORRECT_ANSWER_MAP.put(18, "A");
        CORRECT_ANSWER_MAP.put(19, "C");
        CORRECT_ANSWER_MAP.put(20, "D");
        CORRECT_ANSWER_MAP.put(21, "F");
        CORRECT_ANSWER_MAP.put(22, "C");
        CORRECT_ANSWER_MAP.put(23, "E");
        CORRECT_ANSWER_MAP.put(24, "B");

        // ================= B 类同比较能力（25–36） =================
        CORRECT_ANSWER_MAP.put(25, "B");
        CORRECT_ANSWER_MAP.put(26, "F");
        CORRECT_ANSWER_MAP.put(27, "A");
        CORRECT_ANSWER_MAP.put(28, "B");
        CORRECT_ANSWER_MAP.put(29, "A");
        CORRECT_ANSWER_MAP.put(30, "C");
        CORRECT_ANSWER_MAP.put(31, "E");
        CORRECT_ANSWER_MAP.put(32, "F");
        CORRECT_ANSWER_MAP.put(33, "D");
        CORRECT_ANSWER_MAP.put(34, "C");
        CORRECT_ANSWER_MAP.put(35, "D");
        CORRECT_ANSWER_MAP.put(36, "E");

        // ================= C 比较推理能力（37–48） =================
        CORRECT_ANSWER_MAP.put(37, "H");
        CORRECT_ANSWER_MAP.put(38, "B");
        CORRECT_ANSWER_MAP.put(39, "C");
        CORRECT_ANSWER_MAP.put(40, "H");
        CORRECT_ANSWER_MAP.put(41, "G");
        CORRECT_ANSWER_MAP.put(42, "D");
        CORRECT_ANSWER_MAP.put(43, "E");
        CORRECT_ANSWER_MAP.put(44, "A");
        CORRECT_ANSWER_MAP.put(45, "G");
        CORRECT_ANSWER_MAP.put(46, "F");
        CORRECT_ANSWER_MAP.put(47, "A");
        CORRECT_ANSWER_MAP.put(48, "B");

        // ================= D 系列关系能力（49–60） =================
        CORRECT_ANSWER_MAP.put(49, "C");
        CORRECT_ANSWER_MAP.put(50, "D");
        CORRECT_ANSWER_MAP.put(51, "C");
        CORRECT_ANSWER_MAP.put(52, "G");
        CORRECT_ANSWER_MAP.put(53, "H");
        CORRECT_ANSWER_MAP.put(54, "F");
        CORRECT_ANSWER_MAP.put(55, "E");
        CORRECT_ANSWER_MAP.put(56, "D");
        CORRECT_ANSWER_MAP.put(57, "A");
        CORRECT_ANSWER_MAP.put(58, "B");
        CORRECT_ANSWER_MAP.put(59, "E");
        CORRECT_ANSWER_MAP.put(60, "F");

        // ================= E 抽象推理能力（61–72） =================
        CORRECT_ANSWER_MAP.put(61, "G");
        CORRECT_ANSWER_MAP.put(62, "F");
        CORRECT_ANSWER_MAP.put(63, "H");
        CORRECT_ANSWER_MAP.put(64, "B");
        CORRECT_ANSWER_MAP.put(65, "A");
        CORRECT_ANSWER_MAP.put(66, "E");
        CORRECT_ANSWER_MAP.put(67, "A");
        CORRECT_ANSWER_MAP.put(68, "F");
        CORRECT_ANSWER_MAP.put(69, "C");
        CORRECT_ANSWER_MAP.put(70, "B");
        CORRECT_ANSWER_MAP.put(71, "D");
        CORRECT_ANSWER_MAP.put(72, "B");
    }

    private static Map<Integer, String> parseAnswerDetails(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> raw = mapper.readValue(json, new TypeReference<>() {
            });
            Map<Integer, String> result = new HashMap<>();
            raw.forEach((k, v) -> result.put(Integer.parseInt(k), v));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("answer_details 解析失败", e);
        }
    }

    private static Map<String, Integer> calculateScores(Map<Integer, String> userAnswers) {
        Map<String, Integer> result = new LinkedHashMap<>();
        int total = 0;

        for (var entry : CATEGORY_RANGES.entrySet()) {
            int score = 0;
            for (int i = entry.getValue()[0]; i <= entry.getValue()[1]; i++) {
                if (CORRECT_ANSWER_MAP.containsKey(i)
                        && CORRECT_ANSWER_MAP.get(i).equals(userAnswers.get(i))) {
                    score++;
                }
            }
            result.put(entry.getKey(), score);
            total += score;
        }
        result.put("total", total);
        return result;
    }


    private static List<Map<String, Object>> buildWrongList(
            Map<Integer, String> userAnswers,
            Map<Integer, QuestionDetail> questionDetailMap
    ) {
        List<Map<String, Object>> wrongList = new ArrayList<>();

        for (Map.Entry<Integer, String> e : CORRECT_ANSWER_MAP.entrySet()) {
            Integer index = e.getKey();
            String correct = e.getValue();
            String user = userAnswers.get(index);

            if (user != null && !user.equals(correct)) {

                QuestionDetail detail = questionDetailMap.get(index);
                if (detail == null) continue;

                List<Map<String, Object>> optionViews = new ArrayList<>();

                for (QuestionDetail.Option opt : detail.getOptions()) {
                    Map<String, Object> ov = new HashMap<>();
                    ov.put("option", opt.getOption());
                    ov.put("img", opt.getDetails().getUrl());
                    ov.put("isUser", opt.getOption().equals(user));
                    ov.put("isCorrect", opt.getOption().equals(correct));
                    optionViews.add(ov);
                }

                Map<String, Object> w = new HashMap<>();
                w.put("index", index);
                w.put("questionImg", detail.getQuestion().getUrl());
                w.put("options", optionViews);
                w.put("userAnswer", user);
                w.put("correctAnswer", correct);
                w.put("explanation", "本题考查图形关系与抽象推理能力。");

                wrongList.add(w);
            }
        }
        return wrongList;
    }


    public static IQResult calculate(int age, int rawScore) {

        // 年龄边界处理
        if (age < MIN_AGE) age = MIN_AGE;
        if (age > MAX_AGE) age = MAX_AGE;

        // 年龄 -> index
        int ageIndex = age - MIN_AGE;

        IQLevel level = null;
        int matchedScore = 0;

        // 找到原始分数所在的档位（从高到低查找）
        for (IQLevel iqLevel : IQ_TABLE) {
            int score = iqLevel.getScoreByAge()[ageIndex];
            if (rawScore >= score) {
                level = iqLevel;
                matchedScore = score;
                break;
            }
        }

        // 如果仍找不到，就用最低档位
        if (level == null) {
            level = IQ_TABLE.get(IQ_TABLE.size() - 1);
            matchedScore = level.getScoreByAge()[ageIndex];
        }

        // IQ 计算公式
        int iq = (int) Math.ceil(rawScore * 1.0 / matchedScore * level.getBaseIQ());

        // 输出
        IQResult result = new IQResult();
        result.setIq(iq);
        result.setDesc(level.getDescNew());
        result.setPercentile(level.getPercentile());
        result.setEval(level.getEval());
        result.setSummary(level.getSummary());

        return result;
    }


    public static void generateReport(int age, String answerDetailsJson, Map<Integer, QuestionDetail> questionDetailMap, String fileName, Integer elapsedTime) throws Exception {


        // Thymeleaf config
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();

        Map<Integer, String> userAnswers = parseAnswerDetails(answerDetailsJson);

        // 1. 得分
        Map<String, Integer> scores = calculateScores(userAnswers);
        int totalScore = scores.get("total");

        // 2. IQ
        IQResult iqResult = calculate(age, totalScore);

        // 3. 错题
        List<Map<String, Object>> wrongList = buildWrongList(userAnswers, questionDetailMap);

        // ===== 雷达图 =====
        List<String> radarLabels = new ArrayList<>();
        List<Integer> radarValues = new ArrayList<>();

        for (String k : CATEGORY_RANGES.keySet()) {
            radarLabels.add(k);
            radarValues.add(scores.get(k));
        }

        // ===== 维度分析表 =====
        List<Map<String, Object>> dimensionList = new ArrayList<>();
        char dimCode = 'A';
        for (Map.Entry<String, int[]> entry : CATEGORY_RANGES.entrySet()) {
            String dimension = entry.getKey();
            int[] range = entry.getValue();

            Map<String, Object> row = new HashMap<>();
            row.put("code", String.valueOf(dimCode));
            row.put("name", dimension);
            row.put("range", range[0] + "–" + range[1]);
            row.put("score", scores.get(dimension));

            dimensionList.add(row);
            dimCode++;
        }

        // ===== Thymeleaf =====
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");

        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        Context ctx = new Context();
        ctx.setVariable("age", age);
        ctx.setVariable("testDate", new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // 答对题数（独立统计用户答案与正确答案匹配数）
        int correctCount = 0;
        for (Map.Entry<Integer, String> entry : CORRECT_ANSWER_MAP.entrySet()) {
            String userAnswer = userAnswers.get(entry.getKey());
            if (userAnswer != null && userAnswer.equals(entry.getValue())) {
                correctCount++;
            }
        }
        ctx.setVariable("correctCount", correctCount);

        // 答题耗时格式化
        String elapsedTimeStr = "--";
        if (elapsedTime != null && elapsedTime > 0) {
            int minutes = elapsedTime / 60;
            int seconds = elapsedTime % 60;
            if (minutes > 0) {
                elapsedTimeStr = minutes + "分" + seconds + "秒";
            } else {
                elapsedTimeStr = seconds + "秒";
            }
        }
        ctx.setVariable("elapsedTime", elapsedTimeStr);

        ctx.setVariable("score", iqResult.getIq());
        ctx.setVariable("iqDesc", iqResult.getDesc());
        ctx.setVariable("percentile", iqResult.getPercentile());
        ctx.setVariable("result", iqResult.getEval());
        ctx.setVariable("analysisHtml", iqResult.getSummary());
        ctx.setVariable("radarLabels", radarLabels);
        ctx.setVariable("radarValues", radarValues);
        ctx.setVariable("wrongList", wrongList);
        ctx.setVariable("dimensionList", dimensionList);
        // 图表图片：使用网络图片路径
        ctx.setVariable("iqDistributionImg", "http://jia.szzxsw.cn/images/iq_distribution.png");
        ctx.setVariable("iqAgeChartImg", "http://jia.szzxsw.cn/images/iq_age_chart.jpg");

        String html = engine.process("RuiWenReport", ctx);

        try (FileWriter fw = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            fw.write(html);
        }
    }

    public static void main(String[] args) throws Exception {

        int age = 10;

        String answerDetailsJson =
                "{"
                        + "\"1\":\"D\",\"2\":\"A\",\"3\":\"A\",\"4\":\"A\",\"5\":\"F\",\"6\":\"C\",\"7\":\"A\",\"8\":\"A\","
                        + "\"9\":\"B\",\"10\":\"C\",\"11\":\"D\",\"12\":\"A\","
                        + "\"13\":\"D\",\"14\":\"E\",\"15\":\"A\",\"16\":\"F\",\"17\":\"B\",\"18\":\"A\",\"19\":\"C\",\"20\":\"D\","
                        + "\"21\":\"F\",\"22\":\"C\",\"23\":\"E\",\"24\":\"B\","
                        + "\"25\":\"B\",\"26\":\"F\",\"27\":\"A\",\"28\":\"B\",\"29\":\"A\",\"30\":\"C\",\"31\":\"E\",\"32\":\"F\","
                        + "\"33\":\"D\",\"34\":\"C\",\"35\":\"D\",\"36\":\"E\","
                        + "\"37\":\"H\",\"38\":\"B\",\"39\":\"C\",\"40\":\"H\",\"41\":\"G\",\"42\":\"D\",\"43\":\"E\",\"44\":\"A\","
                        + "\"45\":\"G\",\"46\":\"F\",\"47\":\"A\",\"48\":\"B\","
                        + "\"49\":\"C\",\"50\":\"D\",\"51\":\"C\",\"52\":\"G\",\"53\":\"H\",\"54\":\"F\",\"55\":\"E\",\"56\":\"D\","
                        + "\"57\":\"A\",\"58\":\"B\",\"59\":\"E\",\"60\":\"F\","
                        + "\"61\":\"G\",\"62\":\"F\",\"63\":\"H\",\"64\":\"B\",\"65\":\"A\",\"66\":\"E\","
                        + "\"67\":\"A\",\"68\":\"F\",\"69\":\"C\",\"70\":\"B\",\"71\":\"D\",\"72\":\"B\""
                        + "}";

        // ✅ 一行生成全部题目详情（1–72）
        Map<Integer, QuestionDetail> questionMap = buildAllQuestionDetails();

        // ===== 生成报告 =====
        generateReport(age, answerDetailsJson, questionMap, "D:/raven_report.html", 1200);

        System.out.println("报告生成完成：D:/raven_report.html");
    }

    private static Map<Integer, QuestionDetail> buildAllQuestionDetails() {

        Map<Integer, QuestionDetail> map = new HashMap<>();

        // 图片基础路径（强烈建议统一）
        String baseUrl = "http://localhost:19999/evaluation_system/images/";

        for (int i = 1; i <= 72; i++) {

            QuestionDetail detail = new QuestionDetail();

            // ===== question =====
            QuestionDetail.Question question = new QuestionDetail.Question();
            question.setUrl(baseUrl + "A" + i + ".png");
            detail.setQuestion(question);

            // ===== options =====
            List<QuestionDetail.Option> options = new ArrayList<>();

            // 默认 A–F
            int optionCount = 6;

            // C 组（37–48）为 A–H
            if (i >= 37 && i <= 48) {
                optionCount = 8;
            }

            for (int j = 1; j <= optionCount; j++) {

                QuestionDetail.Option option = new QuestionDetail.Option();
                option.setOption(String.valueOf((char) ('A' + j - 1)));

                QuestionDetail.Option.Details details =
                        new QuestionDetail.Option.Details();
                details.setUrl(
                        baseUrl + "A" + i + "_0" + j + ".png"
                );

                option.setDetails(details);
                options.add(option);
            }

            detail.setOptions(options);
            map.put(i, detail);
        }

        return map;
    }


}
