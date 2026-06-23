import tkinter as tk
from tkinter import messagebox

from services.auth_service import AuthService


class LoginWindow:
    def __init__(self, root):
        self.root = root
        self.auth = AuthService()

        self.root.title("Đăng nhập")
        self.root.geometry("360x260")
        self.root.resizable(False, False)

        self.frame = tk.Frame(root, padx=25, pady=25)
        self.frame.pack(expand=True, fill="both")

        tk.Label(
            self.frame,
            text="QUẢN LÝ CHI TIÊU",
            font=("Arial", 16, "bold")
        ).pack(pady=(0, 15))

        tk.Label(self.frame, text="Username").pack(anchor="w")
        self.username = tk.Entry(self.frame, width=35)
        self.username.pack(pady=(0, 10))

        tk.Label(self.frame, text="Password").pack(anchor="w")
        self.password = tk.Entry(self.frame, width=35, show="*")
        self.password.pack(pady=(0, 15))

        tk.Button(
            self.frame,
            text="Đăng nhập",
            width=25,
            command=self.login
        ).pack(pady=3)

        tk.Button(
            self.frame,
            text="Đăng ký tài khoản mới",
            width=25,
            command=self.open_register_window
        ).pack(pady=3)

        self.username.focus()

        self.root.bind("<Return>", lambda event: self.login())

    def login(self):
        user = self.username.get()
        pwd = self.password.get()

        if self.auth.login(user, pwd):
            messagebox.showinfo("OK", "Đăng nhập thành công")

            self.frame.destroy()

            from ui.dashboard import Dashboard
            Dashboard(self.root)

        else:
            messagebox.showerror("Lỗi", "Sai username hoặc password")

    def open_register_window(self):
        register_window = tk.Toplevel(self.root)
        register_window.title("Đăng ký tài khoản")
        register_window.geometry("340x260")
        register_window.resizable(False, False)

        frame = tk.Frame(register_window, padx=25, pady=25)
        frame.pack(expand=True, fill="both")

        tk.Label(
            frame,
            text="ĐĂNG KÝ TÀI KHOẢN",
            font=("Arial", 14, "bold")
        ).pack(pady=(0, 15))

        tk.Label(frame, text="Username").pack(anchor="w")
        entry_username = tk.Entry(frame, width=32)
        entry_username.pack(pady=(0, 10))

        tk.Label(frame, text="Password").pack(anchor="w")
        entry_password = tk.Entry(frame, width=32, show="*")
        entry_password.pack(pady=(0, 10))

        tk.Label(frame, text="Nhập lại password").pack(anchor="w")
        entry_confirm = tk.Entry(frame, width=32, show="*")
        entry_confirm.pack(pady=(0, 15))

        def register_account():
            username = entry_username.get()
            password = entry_password.get()
            confirm = entry_confirm.get()

            if password != confirm:
                messagebox.showerror("Lỗi", "Mật khẩu nhập lại không khớp")
                return

            result = self.auth.register(username, password)

            if result == "EMPTY":
                messagebox.showwarning("Thiếu thông tin", "Vui lòng nhập username và password")

            elif result == "EXISTS":
                messagebox.showerror("Lỗi", "Username đã tồn tại")

            elif result == "OK":
                messagebox.showinfo("Thành công", "Đăng ký tài khoản thành công")

                self.username.delete(0, tk.END)
                self.username.insert(0, username)

                self.password.delete(0, tk.END)
                self.password.focus()

                register_window.destroy()

        tk.Button(
            frame,
            text="Đăng ký",
            width=22,
            command=register_account
        ).pack()

        entry_username.focus()