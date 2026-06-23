import tkinter as tk
from tkinter import ttk
from tkinter import messagebox
from datetime import datetime
import uuid
import os

from services.transactions_service import TransactionService
from services.report_service import ReportService
from services.analysis_service import AnalysisService
from models.transactions import Transaction


class Dashboard:
    def __init__(self, root):
        self.root = root
        self.service = TransactionService()
        self.report_service = ReportService()
        self.analysis_service = AnalysisService()

        self.root.title("Quản lý chi tiêu")
        self.root.geometry("900x560")

        self.create_form()
        self.create_table()
        self.create_buttons()

        self.load_data()

    def create_form(self):
        form_frame = tk.LabelFrame(
            self.root,
            text="Thêm khoản thu / chi",
            padx=10,
            pady=10
        )
        form_frame.pack(
            fill="x",
            padx=10,
            pady=10
        )

        tk.Label(form_frame, text="Ngày").grid(row=0, column=0, padx=5, pady=5)
        self.date_entry = tk.Entry(form_frame, width=15)
        self.date_entry.grid(row=0, column=1, padx=5, pady=5)
        self.date_entry.insert(0, datetime.now().strftime("%Y-%m-%d"))

        tk.Label(form_frame, text="Loại").grid(row=0, column=2, padx=5, pady=5)
        self.type_combo = ttk.Combobox(
            form_frame,
            values=["Thu", "Chi"],
            width=12,
            state="readonly"
        )
        self.type_combo.grid(row=0, column=3, padx=5, pady=5)
        self.type_combo.current(1)

        tk.Label(form_frame, text="Danh mục").grid(row=0, column=4, padx=5, pady=5)
        self.category_entry = tk.Entry(form_frame, width=18)
        self.category_entry.grid(row=0, column=5, padx=5, pady=5)

        tk.Label(form_frame, text="Số tiền").grid(row=1, column=0, padx=5, pady=5)
        self.amount_entry = tk.Entry(form_frame, width=15)
        self.amount_entry.grid(row=1, column=1, padx=5, pady=5)

        tk.Label(form_frame, text="Ghi chú").grid(row=1, column=2, padx=5, pady=5)
        self.note_entry = tk.Entry(form_frame, width=45)
        self.note_entry.grid(row=1, column=3, columnspan=3, padx=5, pady=5)

        tk.Button(
            form_frame,
            text="Thêm dữ liệu",
            width=15,
            command=self.add_transaction
        ).grid(row=2, column=0, columnspan=2, pady=10)

    def create_table(self):
        table_frame = tk.Frame(self.root)
        table_frame.pack(
            fill="both",
            expand=True,
            padx=10,
            pady=5
        )

        columns = (
            "ID",
            "Date",
            "Type",
            "Category",
            "Amount",
            "Note"
        )

        self.tree = ttk.Treeview(
            table_frame,
            columns=columns,
            show="headings"
        )

        for col in columns:
            self.tree.heading(col, text=col)

        self.tree.column("ID", width=120)
        self.tree.column("Date", width=100)
        self.tree.column("Type", width=80)
        self.tree.column("Category", width=140)
        self.tree.column("Amount", width=120)
        self.tree.column("Note", width=300)

        scrollbar_y = ttk.Scrollbar(
            table_frame,
            orient="vertical",
            command=self.tree.yview
        )

        scrollbar_x = ttk.Scrollbar(
            table_frame,
            orient="horizontal",
            command=self.tree.xview
        )

        self.tree.configure(
            yscrollcommand=scrollbar_y.set,
            xscrollcommand=scrollbar_x.set
        )

        self.tree.pack(
            side="left",
            fill="both",
            expand=True
        )

        scrollbar_y.pack(
            side="right",
            fill="y"
        )

        scrollbar_x.pack(
            side="bottom",
            fill="x"
        )

    def create_buttons(self):
        button_frame = tk.Frame(self.root)
        button_frame.pack(
            fill="x",
            padx=10,
            pady=10
        )

        tk.Button(
            button_frame,
            text="Tải lại dữ liệu",
            width=16,
            command=self.load_data
        ).pack(side="left", padx=5)

        tk.Button(
            button_frame,
            text="Xóa dòng đã chọn",
            width=16,
            command=self.delete_selected
        ).pack(side="left", padx=5)

        tk.Button(
            button_frame,
            text="Xuất báo cáo Excel",
            width=18,
            command=self.export_report
        ).pack(side="left", padx=5)

        tk.Button(
            button_frame,
            text="Xem nhận xét chi tiêu",
            width=20,
            command=self.show_advice
        ).pack(side="left", padx=5)

    def load_data(self):
        for item in self.tree.get_children():
            self.tree.delete(item)

        df = self.service.get_all()

        for _, row in df.iterrows():
            self.tree.insert(
                "",
                "end",
                values=[
                    row["ID"],
                    row["Date"],
                    row["Type"],
                    row["Category"],
                    row["Amount"],
                    row["Note"]
                ]
            )

    def add_transaction(self):
        date = self.date_entry.get().strip()
        trans_type = self.type_combo.get().strip()
        category = self.category_entry.get().strip()
        amount = self.amount_entry.get().strip()
        note = self.note_entry.get().strip()

        if date == "":
            messagebox.showwarning("Thiếu dữ liệu", "Vui lòng nhập ngày")
            return

        if trans_type == "":
            messagebox.showwarning("Thiếu dữ liệu", "Vui lòng chọn loại Thu hoặc Chi")
            return

        if category == "":
            messagebox.showwarning("Thiếu dữ liệu", "Vui lòng nhập danh mục")
            return

        if amount == "":
            messagebox.showwarning("Thiếu dữ liệu", "Vui lòng nhập số tiền")
            return

        try:
            amount = float(amount)
        except ValueError:
            messagebox.showerror("Lỗi", "Số tiền phải là số")
            return

        trans_id = str(uuid.uuid4())[:8]

        transaction = Transaction(
            trans_id=trans_id,
            date=date,
            trans_type=trans_type,
            category=category,
            amount=amount,
            note=note
        )

        self.service.add(transaction)

        messagebox.showinfo("Thành công", "Đã thêm dữ liệu")

        self.clear_form()
        self.load_data()

    def clear_form(self):
        self.category_entry.delete(0, tk.END)
        self.amount_entry.delete(0, tk.END)
        self.note_entry.delete(0, tk.END)

        self.date_entry.delete(0, tk.END)
        self.date_entry.insert(0, datetime.now().strftime("%Y-%m-%d"))

        self.type_combo.current(1)

    def delete_selected(self):
        selected_item = self.tree.selection()

        if not selected_item:
            messagebox.showwarning("Chưa chọn dòng", "Vui lòng chọn dòng cần xóa")
            return

        confirm = messagebox.askyesno(
            "Xác nhận",
            "Bạn có chắc muốn xóa dòng này không?"
        )

        if not confirm:
            return

        item = self.tree.item(selected_item)
        trans_id = item["values"][0]

        self.service.delete(trans_id)

        messagebox.showinfo("Thành công", "Đã xóa dữ liệu")

        self.load_data()

    def export_report(self):
        try:
            file_path = self.report_service.export_excel()

            messagebox.showinfo(
                "Xuất báo cáo thành công",
                f"Đã xuất báo cáo tại:\n{file_path}"
            )

            os.startfile(file_path)

        except Exception as e:
            messagebox.showerror(
                "Lỗi xuất báo cáo",
                f"Không thể xuất báo cáo.\nChi tiết lỗi:\n{e}"
            )

    def show_advice(self):
        advice_text = self.analysis_service.advice()

        advice_window = tk.Toplevel(self.root)
        advice_window.title("Nhận xét chi tiêu")
        advice_window.geometry("650x520")

        text_box = tk.Text(
            advice_window,
            wrap="word",
            padx=10,
            pady=10
        )
        text_box.pack(
            fill="both",
            expand=True
        )

        text_box.insert("1.0", advice_text)
        text_box.config(state="disabled")