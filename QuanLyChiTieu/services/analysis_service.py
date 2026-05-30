import pandas as pd

FILE = "data/transactions.csv"


class AnalysisService:

    def top_category(self):

        df = pd.read_csv(FILE)

        expense = df[
            df["Type"] == "Chi"
        ]

        result = (
            expense.groupby(
                "Category"
            )["Amount"]
            .sum()
            .sort_values(
                ascending=False
            )
        )

        return result

    def advice(self):

        result = self.top_category()

        if result.empty:
            return "Chưa có dữ liệu"

        top = result.index[0]

        return (
            f"Bạn đang chi nhiều nhất cho: {top}"
        )