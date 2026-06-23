import os
import pandas as pd


BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_DIR = os.path.join(BASE_DIR, "data")
FILE = os.path.join(DATA_DIR, "transactions.csv")


class AnalysisService:
    def __init__(self):
        os.makedirs(DATA_DIR, exist_ok=True)

        if not os.path.exists(FILE):
            df = pd.DataFrame(
                columns=[
                    "ID",
                    "Date",
                    "Type",
                    "Category",
                    "Amount",
                    "Note"
                ]
            )
            df.to_csv(FILE, index=False)

    def read_data(self):
        df = pd.read_csv(FILE).fillna("")

        if df.empty:
            return df

        df["Amount"] = pd.to_numeric(df["Amount"], errors="coerce").fillna(0)

        return df

    def top_category(self):
        df = self.read_data()

        if df.empty:
            return None

        expense = df[df["Type"] == "Chi"]

        if expense.empty:
            return None

        result = (
            expense
            .groupby("Category")["Amount"]
            .sum()
            .sort_values(ascending=False)
        )

        if result.empty:
            return None

        return result

    def advice(self):
        df = self.read_data()

        if df.empty:
            return "Chưa có dữ liệu để phân tích. Bạn hãy nhập thêm các khoản thu và chi trước."

        income = df[df["Type"] == "Thu"]["Amount"].sum()
        expense = df[df["Type"] == "Chi"]["Amount"].sum()
        balance = income - expense

        top_result = self.top_category()

        lines = []

        lines.append("NHẬN XÉT TỔNG QUAN VỀ CHI TIÊU")
        lines.append("--------------------------------")
        lines.append(f"Tổng thu: {income:,.0f} VND")
        lines.append(f"Tổng chi: {expense:,.0f} VND")
        lines.append(f"Số dư: {balance:,.0f} VND")
        lines.append("")

        if income == 0 and expense > 0:
            lines.append("Bạn đang có khoản chi nhưng chưa ghi nhận khoản thu nào.")
            lines.append("Nên bổ sung dữ liệu thu nhập để phần mềm đánh giá chính xác hơn.")
            lines.append("")

        if income > 0:
            expense_rate = expense / income * 100
            lines.append(f"Tỷ lệ chi tiêu trên thu nhập: {expense_rate:.2f}%")

            if expense_rate >= 90:
                lines.append("Cảnh báo: Mức chi tiêu đang rất cao so với thu nhập.")
                lines.append("Bạn nên rà soát các khoản chi không bắt buộc và giảm bớt chi tiêu trong tháng tới.")
            elif expense_rate >= 70:
                lines.append("Mức chi tiêu tương đối cao.")
                lines.append("Bạn vẫn còn dư tiền, nhưng nên kiểm soát kỹ các khoản chi lớn.")
            elif expense_rate >= 50:
                lines.append("Mức chi tiêu ở mức trung bình.")
                lines.append("Bạn có thể tiếp tục theo dõi thêm để tối ưu ngân sách.")
            else:
                lines.append("Mức chi tiêu đang khá tốt.")
                lines.append("Bạn đang giữ được tỷ lệ chi thấp so với thu nhập.")

            lines.append("")

        if balance < 0:
            lines.append("Số dư đang âm, nghĩa là tổng chi lớn hơn tổng thu.")
            lines.append("Bạn cần ưu tiên cắt giảm các khoản chi không cần thiết.")
            lines.append("")
        elif balance == 0:
            lines.append("Thu và chi đang cân bằng.")
            lines.append("Tuy nhiên, bạn chưa có phần dư để tiết kiệm.")
            lines.append("")
        else:
            lines.append("Bạn đang có số dư dương.")
            lines.append("Có thể cân nhắc chia phần dư cho tiết kiệm, đầu tư hoặc quỹ dự phòng.")
            lines.append("")

        if top_result is not None:
            top_category = top_result.index[0]
            top_amount = top_result.iloc[0]
            total_expense = top_result.sum()

            top_rate = top_amount / total_expense * 100 if total_expense > 0 else 0

            lines.append("DANH MỤC CHI NHIỀU NHẤT")
            lines.append("--------------------------------")
            lines.append(f"Bạn đang chi nhiều nhất cho: {top_category}")
            lines.append(f"Số tiền: {top_amount:,.0f} VND")
            lines.append(f"Tỷ trọng trong tổng chi: {top_rate:.2f}%")
            lines.append("")

            if top_rate >= 50:
                lines.append("Danh mục này chiếm hơn một nửa tổng chi tiêu.")
                lines.append("Bạn nên kiểm tra xem khoản này có thật sự cần thiết không.")
            elif top_rate >= 30:
                lines.append("Danh mục này chiếm tỷ trọng khá lớn.")
                lines.append("Bạn nên đặt ngân sách giới hạn cho danh mục này.")
            else:
                lines.append("Chi tiêu chưa bị lệ thuộc quá nhiều vào một danh mục duy nhất.")

            lines.append("")
            lines.append("CHI TIÊU THEO DANH MỤC")
            lines.append("--------------------------------")

            for category, amount in top_result.items():
                rate = amount / total_expense * 100 if total_expense > 0 else 0
                lines.append(f"- {category}: {amount:,.0f} VND ({rate:.2f}%)")

        return "\n".join(lines)