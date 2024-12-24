// BHXH = 8%
final double socialInsuranceRate = 0.08;

// BHYT = 1.5%
final double healthInsuranceRate = 0.015;

// BH thất nghiệp = 1%
final double unemploymentInsuranceRate = 0.01;

// Thu nhập giảm trừ gia cảnh miễn thuế (11.000.000 VNĐ)
final long nonTaxedIncome = 11_000_000L;

// Giảm trừ người phụ thuộc (4.400.000 VNĐ/người)
final long deductionPerDependant = 4_400_000L;

// Lương cơ bản đóng BH (lấy mốc 5.100.000 VNĐ)
final long minBasicSalary = 5_100_000L;

static final DecimalFormat FORMATTER = new DecimalFormat(
        "#,###.##",
        DecimalFormatSymbols.getInstance(Locale.of(
                "vi", "vn")
        )
);

enum ProgressiveTaxes {

    LEVEL_0(0.0, 0.0),
    LEVEL_1(0.05, 5_000_000.0),
    LEVEL_2(0.1, 10_000_000.0),
    LEVEL_3(0.15, 18_000_000.0),
    LEVEL_4(0.2, 32_000_000.0),
    LEVEL_5(0.25, 52_000_000.0),
    LEVEL_6(0.3, 80_000_000.0),
    LEVEL_7(0.35, Double.MAX_VALUE);

    final double rate;
    final double threshold;

    ProgressiveTaxes(double rate, double threshold) {
        this.rate = rate;
        this.threshold = threshold;
    }
}

@SuppressWarnings("preview")
Number input(String prompt, double minValue) {
    while (true) {
        var result = Double.parseDouble(
                readln(prompt)
        );

        if (result >= minValue) {
            return result;
        }

        println("Number cannot be less than " + minValue);
    }
}

String format(double value) {
    return FORMATTER.format(
            BigDecimal.valueOf(value)
                    .setScale(2, RoundingMode.CEILING)
    ) + " VND";
}

@SuppressWarnings("preview")
void main() {
    var basicIncome = input(
            "Enter basic income: ",
            minBasicSalary
    ).doubleValue();

    // thu nhập trước thuế phải lớn hơn lương đóng BH
    var totalIncome = input(
            "Enter gross income: ",
            basicIncome
    ).doubleValue();


    var numberOfDependants = input(
            "Number of dependants: ",
            0
    ).intValue();

    // tiền đóng bảo hiểm
    var totalInsurance =
            (socialInsuranceRate
             + healthInsuranceRate
             + unemploymentInsuranceRate)
            * basicIncome;

    // thu nhập chịu thuế
    var taxableIncome = totalIncome
                        - totalInsurance
                        - nonTaxedIncome
                        - deductionPerDependant * numberOfDependants;

    // bậc thuế
    var taxLevel = 0;

    // thuế
    var taxedAmount = 0.0;

    // Bậc thuế lũy tiến
    var progressiveTaxes = ProgressiveTaxes.values();

    while (taxableIncome > progressiveTaxes[taxLevel].threshold) {
        var nextLevelRate = progressiveTaxes[taxLevel + 1].rate;
        var nextLevelThreshold = progressiveTaxes[taxLevel + 1].threshold;

        var incomeDelta = taxableIncome < nextLevelThreshold
                // thu nhập chịu thuế trừ đi ngưỡng thuế bậc hiện tại
                ? taxableIncome - progressiveTaxes[taxLevel].threshold
                // ngưỡng bậc thuế kế tiếp trừ đi bậc thuế
                : nextLevelThreshold - progressiveTaxes[taxLevel].threshold;

        taxedAmount += nextLevelRate * incomeDelta;

        taxLevel++;
    }

    println("");

    println("Insurance amount: "
            + format(totalInsurance));

    println("Taxable income: "
            + format(taxableIncome));

    println("Taxed amount: "
            + format(taxedAmount));

    println("Net income: "
            + format(totalIncome - totalInsurance - taxedAmount));
}