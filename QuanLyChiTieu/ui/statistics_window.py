import tkinter as tk
from tkinter import ttk
from tkinter import messagebox

from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.figure import Figure

from services.statistics_service import StatisticsService


class StatisticsWindow:
    def __init__(self, root):
        self.root = root
        self.service = StatisticsService()

        self.window = tk.Toplevel(root)
        self.window.title("Thống kê và biểu đồ chi tiêu")
        self.window.geometry("950x650")

        self.notebook = ttk.Notebook(self.window)
        self.notebook.pack(fill="both", expand=True)

        self.create_overview_tab()
        self.create_category_pie_tab()
        self.create_category_bar_tab()
        self.create_trend_tab()
        self.create_monthly_tab()

    def create_overview_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text="Tổng quan")

        overview = self.service.overview()

        title = tk.Label(
            frame,
            text="THỐNG KÊ TỔNG QUAN",
            font=("Arial", 18, "bold")
        )
        title.pack(pady=20)

        info_frame = tk.Frame(frame)
        info_frame.pack(pady=10)

        data = [
            ("Tổng thu", overview["income"]),
            ("Tổng chi", overview["expense"]),
            ("Số dư", overview["balance"]),
            ("Số giao dịch", overview["transaction_count"]),
            ("Chi trung bình / giao dịch", overview["avg_expense"]),
            ("Khoản chi lớn nhất", overview["max_expense"]),
            ("Khoản chi nhỏ nhất", overview["min_expense"])
        ]

        for index, item in enumerate(data):
            label, value = item

            tk.Label(
                info_frame,
                text=label,
                font=("Arial", 12, "bold"),
                width=25,
                anchor="w"
            ).grid(row=index, column=0, padx=10, pady=8)

            if label == "Số giao dịch":
                display_value = f"{value}"
            else:
                display_value = f"{value:,.0f} VND"

            tk.Label(
                info_frame,
                text=display_value,
                font=("Arial", 12),
                width=25,
                anchor="e"
            ).grid(row=index, column=1, padx=10, pady=8)

        advice = self.make_quick_advice(overview)

        tk.Label(
            frame,
            text="Nhận xét nhanh",
            font=("Arial", 14, "bold")
        ).pack(pady=(25, 5))

        text = tk.Text(
            frame,
            height=7,
            wrap="word",
            padx=10,
            pady=10
        )
        text.pack(fill="x", padx=30)
        text.insert("1.0", advice)
        text.config(state="disabled")

    def make_quick_advice(self, overview):
        income = overview["income"]
        expense = overview["expense"]
        balance = overview["balance"]

        lines = []

        if income == 0 and expense == 0:
            return "Chưa có dữ liệu thu chi để thống kê."

        if income == 0 and expense > 0:
            lines.append("Bạn đang có dữ liệu chi tiêu nhưng chưa ghi nhận khoản thu.")
            lines.append("Nên nhập thêm khoản thu để phần mềm đánh giá chính xác hơn.")
            return "\n".join(lines)

        if income > 0:
            rate = expense / income * 100

            lines.append(f"Tỷ lệ chi tiêu trên thu nhập hiện tại là {rate:.2f}%.")

            if rate >= 90:
                lines.append("Mức chi tiêu rất cao, cần kiểm soát ngay các khoản chi không bắt buộc.")
            elif rate >= 70:
                lines.append("Mức chi tiêu tương đối cao, nên đặt giới hạn ngân sách cho từng danh mục.")
            elif rate >= 50:
                lines.append("Mức chi tiêu ở mức trung bình, vẫn nên theo dõi thêm theo từng tháng.")
            else:
                lines.append("Mức chi tiêu đang khá tốt so với thu nhập.")

        if balance < 0:
            lines.append("Số dư đang âm, tổng chi lớn hơn tổng thu.")
        elif balance > 0:
            lines.append("Số dư đang dương, có thể trích một phần cho tiết kiệm hoặc quỹ dự phòng.")
        else:
            lines.append("Thu và chi đang cân bằng, chưa tạo được khoản dư.")

        return "\n".join(lines)

    def create_category_pie_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text="Tỷ trọng chi")

        df = self.service.expense_by_category()

        if df.empty:
            self.show_empty_message(frame)
            return

        fig = Figure(figsize=(7, 5), dpi=100)
        ax = fig.add_subplot(111)

        ax.pie(
            df["Amount"],
            labels=df["Category"],
            autopct="%1.1f%%",
            startangle=90
        )

        ax.set_title("Tỷ trọng chi tiêu theo danh mục")

        canvas = FigureCanvasTkAgg(fig, master=frame)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True)

    def create_category_bar_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text="Chi theo danh mục")

        df = self.service.expense_by_category()

        if df.empty:
            self.show_empty_message(frame)
            return

        fig = Figure(figsize=(8, 5), dpi=100)
        ax = fig.add_subplot(111)

        ax.bar(
            df["Category"],
            df["Amount"]
        )

        ax.set_title("Tổng chi theo danh mục")
        ax.set_xlabel("Danh mục")
        ax.set_ylabel("Số tiền")

        ax.tick_params(axis="x", rotation=30)

        fig.tight_layout()

        canvas = FigureCanvasTkAgg(fig, master=frame)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True)

    def create_trend_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text="Xu hướng ngày")

        df = self.service.income_expense_by_date()

        if df.empty:
            self.show_empty_message(frame)
            return

        fig = Figure(figsize=(8, 5), dpi=100)
        ax = fig.add_subplot(111)

        ax.plot(
            df["Date"],
            df["Thu"],
            marker="o",
            label="Thu"
        )

        ax.plot(
            df["Date"],
            df["Chi"],
            marker="o",
            label="Chi"
        )

        ax.set_title("Xu hướng thu và chi theo ngày")
        ax.set_xlabel("Ngày")
        ax.set_ylabel("Số tiền")
        ax.legend()

        ax.tick_params(axis="x", rotation=30)

        fig.tight_layout()

        canvas = FigureCanvasTkAgg(fig, master=frame)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True)

    def create_monthly_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text="Theo tháng")

        df = self.service.monthly_summary()

        if df.empty:
            self.show_empty_message(frame)
            return

        table_frame = tk.Frame(frame)
        table_frame.pack(fill="both", expand=True, padx=10, pady=10)

        columns = ("Month", "Thu", "Chi", "Balance")

        tree = ttk.Treeview(
            table_frame,
            columns=columns,
            show="headings"
        )

        for col in columns:
            tree.heading(col, text=col)
            tree.column(col, width=150)

        for _, row in df.iterrows():
            tree.insert(
                "",
                "end",
                values=[
                    row["Month"],
                    f'{row["Thu"]:,.0f}',
                    f'{row["Chi"]:,.0f}',
                    f'{row["Balance"]:,.0f}'
                ]
            )

        tree.pack(fill="both", expand=True)

        fig = Figure(figsize=(8, 4), dpi=100)
        ax = fig.add_subplot(111)

        ax.plot(
            df["Month"],
            df["Thu"],
            marker="o",
            label="Thu"
        )

        ax.plot(
            df["Month"],
            df["Chi"],
            marker="o",
            label="Chi"
        )

        ax.plot(
            df["Month"],
            df["Balance"],
            marker="o",
            label="Số dư"
        )

        ax.set_title("Tổng hợp thu chi theo tháng")
        ax.set_xlabel("Tháng")
        ax.set_ylabel("Số tiền")
        ax.legend()

        fig.tight_layout()

        canvas = FigureCanvasTkAgg(fig, master=frame)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True, padx=10, pady=10)

    def show_empty_message(self, frame):
        tk.Label(
            frame,
            text="Chưa có dữ liệu để vẽ biểu đồ.",
            font=("Arial", 14)
        ).pack(pady=50)