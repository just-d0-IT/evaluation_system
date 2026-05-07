package com.zhixinsiwei.evaluation_system.demo;

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
 * @ClassName RuiWenIQCalculator.java
 * @Description 瑞文IQ计算类
 * @createTime 2025年11月23日 15:43:00
 */
public class RuiWenIQCalculator {

    // ===== 年龄列定义（5 - 35 共 31 项）=====
    static final int MIN_AGE = 5;
    static final int MAX_AGE = 35;

    // ===== 所有 IQ 档位静态数据（按你表格内容填充）=====
    static final List<IQLevel> IQ_TABLE = Arrays.asList(
            new IQLevel(
                    "130以上", 130, "极高", 99,
                    new int[]{19, 22, 30, 36, 38, 49, 49, 62, 62, 66, 66, 69, 69, 69, 69, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 70, 70, 70, 70, 70, 69, 69},
                    "您的智商水平极高",
                    "您的智商水平超过98%同龄人……（略，按你的表填写）"
            ),
            new IQLevel(
                    "120-129", 120, "超高", 91,
                    new int[]{17, 20, 27, 33, 35, 45, 45, 57, 57, 61, 61, 65, 65, 65, 65, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 65, 65, 65, 65, 65, 65, 65},
                    "您的智商水平超高",
                    "您的智商水平远远高出一般同龄人……"
            ),
            new IQLevel(
                    "110-119", 110, "超常", 75,
                    new int[]{15, 18, 24, 30, 32, 41, 41, 52, 52, 56, 56, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60},
                    "您的智商水平超常",
                    "您具有较好知觉组织和逻辑推理能力……"
            ),
            new IQLevel(
                    "90-109", 90, "正常", 50,
                    new int[]{12, 14, 18, 24, 26, 33, 33, 42, 42, 46, 46, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49},
                    "您的智商水平正常",
                    "您的思维反应较为敏捷……"
            ),
            new IQLevel(
                    "80-89", 80, "中等", 25,
                    new int[]{10, 12, 16, 21, 23, 29, 29, 37, 37, 41, 41, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44},
                    "您的智商水平中等",
                    "思维反应有时不够敏捷……"
            ),
            new IQLevel(
                    "70-79", 70, "偏低", 9,
                    new int[]{8, 10, 14, 18, 20, 25, 25, 32, 32, 36, 36, 38, 38, 38, 38, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 38},
                    "您的智商水平偏低",
                    "知觉组织能力不足……"
            ),
            new IQLevel(
                    "70以下", 60, "低下", 3,
                    new int[]{6, 8, 12, 15, 17, 21, 21, 27, 27, 31, 31, 32, 32, 32, 32, 35, 35, 35, 35, 35, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 32, 32},
                    "您的智商水平低下",
                    "思维混乱，逻辑联系能力差……"
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


    /**
     * 计算每个类别分数和总分
     *
     * @param correct 正确答案，例如 ["4","5","1",...]
     * @param answer  用户作答，例如 ["4","3","1",...]
     * @return Map，包含每个类别得分 和 total 总分
     */
    public static Map<String, Integer> calculateScores(List<String> correct, List<String> answer) {
        Map<String, Integer> result = new LinkedHashMap<>();
        int totalScore = 0;

        for (Map.Entry<String, int[]> entry : CATEGORY_RANGES.entrySet()) {
            String category = entry.getKey();
            int start = entry.getValue()[0] - 1;  // 转换为下标
            int end = entry.getValue()[1] - 1;

            int score = 0;
            for (int i = start; i <= end; i++) {
                if (i < correct.size() && i < answer.size() && correct.get(i).equals(answer.get(i))) {
                    score++;
                }
            }

            result.put(category, score);
            totalScore += score;
        }

        result.put("total", totalScore);
        return result;
    }

    // ===== 核心计算方法 =====
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

    public static void generateHtml() throws Exception {
        // Thymeleaf config
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");    // resources/templates/
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateMode("HTML");
        resolver.setCacheable(false);

        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        Context ctx = new Context();

        // 基础信息
        ctx.setVariable("studentName", "小明");
        ctx.setVariable("age", 10);
        ctx.setVariable("testDate", "2025-11-30");
        ctx.setVariable("score", 43);
        ctx.setVariable("iqDesc", "超常");
        ctx.setVariable("percentile", 88);

        // 雷达数据与标签（5维示例）
        ctx.setVariable("radarValues", Arrays.asList(21, 18, 23, 19, 22));
        ctx.setVariable("radarLabels", Arrays.asList("观察推理", "空间想象", "类比推理", "抽象思维", "逻辑判断"));

        // 常模表（示例）
        List<Map<String, Object>> normList = new ArrayList<>();
        normList.add(Map.of("age", 5, "score", 60, "percentile", 20));
        normList.add(Map.of("age", 6, "score", 65, "percentile", 30));
        normList.add(Map.of("age", 7, "score", 70, "percentile", 45));
        normList.add(Map.of("age", 8, "score", 75, "percentile", 60));
        ctx.setVariable("normList", normList);

        // 正态分布图：可以是远程 URL 或者 data:image/png;base64,....
        // 这里演示使用本地相对路径（执行后打开 HTML，浏览器会请求此路径）
        ctx.setVariable("normalDistributionImg", "static/images/normal_example.png");

        // 百分位曲线
        ctx.setVariable("curveX", Arrays.asList(5, 6, 7, 8, 9, 10));
        ctx.setVariable("curveY", Arrays.asList(60, 65, 70, 75, 80, 85));
        // percentilePoint 可为 [age, y] 的数组或单值（模板中期待 [x,y]）
        ctx.setVariable("percentilePoint", Arrays.asList(10, 85));

        // 评估解读（支持 HTML）
        String analysisHtml = "<p>总体说明：您的被测试者在图形推理方面表现突出，尤其是类比推理与空间想象。</p>"
                + "<p>建议：继续进行逻辑训练与空间想象练习。</p>";
        ctx.setVariable("analysisHtml", analysisHtml);

        // 错题列表（示例 2 个）
        List<Map<String, Object>> wrongList = new ArrayList<>();

        Map<String, Object> w1 = new HashMap<>();
        w1.put("index", 5);
        w1.put("questionImg", "static/images/q5.png"); // 请把资源放在 resources/static/images/
        w1.put("userAnswer", "A");
        w1.put("correctAnswer", "C");
        w1.put("explanation", "此题考查图形旋转与对称变化。");
        // 可选：每个选项包含 details.url
        w1.put("options", List.of(
                Map.of("option", "A", "details", Map.of("url", "static/images/q5_01.png")),
                Map.of("option", "B", "details", Map.of("url", "static/images/q5_02.png")),
                Map.of("option", "C", "details", Map.of("url", "static/images/q5_03.png"))
        ));
        wrongList.add(w1);

        Map<String, Object> w2 = new HashMap<>();
        w2.put("index", 12);
        w2.put("questionImg", "static/images/q12.png");
        w2.put("userAnswer", "C");
        w2.put("correctAnswer", "C");
        w2.put("explanation", "此题属于类比题，正确为 C。");
        wrongList.add(w2);

        ctx.setVariable("wrongList", wrongList);

        // 渲染模板
        String html = engine.process("report", ctx);

        // 输出文件
        String out = "D:\\code\\report_full_output.html";
        try (FileWriter fw = new FileWriter(out, StandardCharsets.UTF_8)) {
            fw.write(html);
        }

        System.out.println("生成完成 -> " + out);
    }


    // 示例运行
    public static void main(String[] args) throws Exception {
        List<String> correct = Arrays.asList("4", "5", "1", "2", "6", "3", "6", "1", "1", "3", "4", "5",
                "4", "5", "1", "6", "2", "1", "3", "4", "6", "3", "5", "2",
                "2", "6", "1", "3", "1", "3", "5", "6", "4", "3", "4", "5",
                "8", "2", "7", "8", "7", "6", "5", "4", "7", "6", "1", "2",
                "3", "4", "3", "6", "8", "6", "1", "4", "1", "2", "5", "2",
                "7", "6", "8", "2", "1", "5", "1", "6", "3", "2", "4", "2");

        List<String> user = Arrays.asList("4", "5", "1", "1", "6", "3", "6", "3", "1", "3", "4", "5",
                "4", "2", "1", "6", "2", "3", "3", "4", "2", "3", "5", "2",
                "2", "6", "1", "8", "1", "3", "5", "6", "4", "1", "4", "5",
                "8", "2", "5", "8", "7", "6", "5", "4", "7", "6", "1", "2",
                "3", "4", "3", "6", "8", "6", "1", "4", "1", "2", "5", "2",
                "7", "6", "8", "2", "1", "5", "1", "6", "3", "2", "4", "1");

        Map<String, Integer> scores = calculateScores(correct, user);
        System.out.println(scores);

        IQResult result = calculate(20, 43);
        System.out.println(result);

        generateHtml();
    }
}
