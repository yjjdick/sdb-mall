package io.sdb.common.utils;

public class MoneyUtils {

    /**
     * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零 要用到正则表达式
     */
    public static String digitUppercase(double n) {
        String fraction[] = {"角", "分"};
        String digit[] = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};

        String head = n < 0 ? "负" : "";
        n = Math.abs(n);

        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if (s.length() < 1) {
            s = "整";
        }
        int integerPart = (int) Math.floor(n);

        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p = "";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart % 10] + unit[1][j] + p;
                integerPart = integerPart / 10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");

    }

    private void number2Invoice(int num) {
        //12345
        String mark[] = new String[]{"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "万"};
        String numCn[] = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

        char[] numArrRev = String.valueOf(num).toCharArray();

        StringBuffer container = new StringBuffer();
        for (int i = 0; i < numArrRev.length; i++) {
            Integer val = Integer.valueOf(String.valueOf(numArrRev[i]));
            String number = numCn[val];
            int x = numArrRev.length - i - 1;
            String sign = mark[x];

            if (val == 0) {
                if (x % 4 != 0) {// 删除单位
                    sign = "";
                }
                if (i < numArrRev.length - 1) {
                    Integer val1 = Integer.parseInt(String.valueOf(numArrRev[i + 1]));
                    if (val == 0 && val == val1) {
                        number = "";
                    } else if (val == 0 && ("万".equals(sign) || "亿".equals(sign))) {
                        number = "";
                    }
                } else if (i == numArrRev.length - 1) {
                    number = "";
                }
            }

            container.append(number + sign);
        }

        System.out.println(num + "-->" + container.toString());
    }

}
