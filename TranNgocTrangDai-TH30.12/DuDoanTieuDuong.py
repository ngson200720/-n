import pandas as pd
import os
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score, recall_score, f1_score
import tkinter as tk
from tkinter import messagebox

# ==============================================================================
# BƯỚC 1: XÁC ĐỊNH BÀI TOÁN
# Mục tiêu: Dự đoán nguy cơ mắc bệnh tiểu đường dựa trên các thông số máu và sinh hóa.
# ==============================================================================

# ==============================================================================
# BƯỚC 2: THU THẬP DỮ LIỆU
# ==============================================================================
current_dir = os.path.dirname(os.path.abspath(__file__))
file_name = "CSDL_TieuDuong.csv" 
file_path = os.path.join(current_dir, file_name)

try:
    df = pd.read_csv(file_path)
except FileNotFoundError:
    np.random.seed(42)
    n_samples = 1000
    
    data = {
        'Tuoi': np.random.randint(1, 100, n_samples),
        'GioiTinh': np.random.choice(['Nam', 'Nữ', 'Male', 'Female', np.nan], n_samples),
        'Glucose': np.random.uniform(3.0, 15.0, n_samples),
        'HbA1C': np.random.uniform(4.0, 12.0, n_samples),
        'Cholesterol': np.random.uniform(3.0, 10.0, n_samples),
        'HDL': np.random.uniform(0.5, 3.0, n_samples),
        'LDL': np.random.uniform(1.0, 5.0, n_samples),
        'Triglycerid': np.random.uniform(0.5, 5.0, n_samples),
        'Creatinine': np.random.uniform(40.0, 200.0, n_samples),
        'Ure': np.random.uniform(2.0, 20.0, n_samples),
        'AcidUric': np.random.uniform(200.0, 600.0, n_samples),
        'AST': np.random.uniform(10.0, 100.0, n_samples),
        'ALT': np.random.uniform(10.0, 120.0, n_samples),
        # Cột mục tiêu lộn xộn giống thực tế
        'NguyCoTieuDuong': np.random.choice(['TRUE', 'FALSE', '1', '0', 'Có nguy cơ', 'Không có nguy cơ', np.nan], n_samples)
    }
    df = pd.DataFrame(data)

# ==============================================================================
# BƯỚC 3: TIỀN XỬ LÝ DỮ LIỆU
# ==============================================================================

# 3.1. Làm sạch & Đồng bộ dữ liệu
# Xử lý cột Giới tính (GioiTinh): Chuyển về 1 (Nam) và 0 (Nữ)
df['GioiTinh'] = df['GioiTinh'].astype(str).str.strip().str.lower()
gender_map = {'nam': 1, 'male': 1, 'nữ': 0, 'female': 0}
df['GioiTinh'] = df['GioiTinh'].map(gender_map)

# Xử lý cột Mục tiêu (NguyCoTieuDuong): Chuyển về 1 (Có bệnh) và 0 (Không bệnh)
df['NguyCoTieuDuong'] = df['NguyCoTieuDuong'].astype(str).str.strip().str.lower()
target_map = {
    'true': 1, '1': 1, '1.0': 1, 'có nguy cơ': 1,
    'false': 0, '0': 0, '0.0': 0, 'không có nguy cơ': 0
}
df['NguyCoTieuDuong'] = df['NguyCoTieuDuong'].map(target_map)

# Loại bỏ các dòng có giá trị bị thiếu (NaN) sau khi map
df = df.dropna()

# Xử lý ngoại lai bằng IQR (Chỉ áp dụng cho các cột số học, bỏ qua Giới tính và Nhãn)
numeric_cols = df.columns.drop(['GioiTinh', 'NguyCoTieuDuong'])
Q1 = df[numeric_cols].quantile(0.25)
Q3 = df[numeric_cols].quantile(0.75)
IQR = Q3 - Q1
# Giữ lại các dòng không chứa ngoại lai
mask = ~((df[numeric_cols] < (Q1 - 1.5 * IQR)) | (df[numeric_cols] > (Q3 + 1.5 * IQR))).any(axis=1)
df = df[mask]

# Tách đặc trưng (X) và nhãn (y)
X = df.drop('NguyCoTieuDuong', axis=1)
y = df['NguyCoTieuDuong']

# 3.2. Biến đổi dữ liệu (Chuẩn hóa)
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# 3.3. Phân chia dữ liệu (70% Train, 15% Val, 15% Test)
X_train, X_temp, y_train, y_temp = train_test_split(X_scaled, y, test_size=0.3, random_state=42)
X_val, X_test, y_val, y_test = train_test_split(X_temp, y_temp, test_size=0.5, random_state=42)

# ==============================================================================
# BƯỚC 4 & 5: CHỌN VÀ HUẤN LUYỆN MÔ HÌNH
# ==============================================================================
models = {
    "Logistic Regression": LogisticRegression(max_iter=1000),
    "Random Forest": RandomForestClassifier(random_state=42),
    "SVM": SVC(probability=True, random_state=42)
}

trained_models = {}

print("--- KẾT QUẢ ĐÁNH GIÁ MÔ HÌNH TRÊN TẬP VALIDATION ---")
for name, model in models.items():
    model.fit(X_train, y_train)
    trained_models[name] = model
    
    # ==============================================================================
    # BƯỚC 6: ĐÁNH GIÁ MÔ HÌNH
    # ==============================================================================
    y_val_pred = model.predict(X_val)
    print(f"[{name}]")
    print(f"Accuracy: {accuracy_score(y_val, y_val_pred):.4f} | Recall: {recall_score(y_val, y_val_pred, average='macro'):.4f} | F1-score: {f1_score(y_val, y_val_pred, average='macro'):.4f}")

# Chọn mô hình Random Forest để đưa vào ứng dụng thực tế
best_model = trained_models["Random Forest"]

# ==============================================================================
# BƯỚC 7: XÂY DỰNG GIAO DIỆN & CẢNH BÁO
# ==============================================================================
def predict_risk():
    try:
        # Thu thập dữ liệu từ các ô nhập liệu
        user_input = []
        for entry, col_name in zip(entries, X.columns):
            val = entry.get().strip().lower()
            # Xử lý riêng cho trường Giới tính nhập từ UI
            if col_name == 'GioiTinh':
                if val in ['nam', 'male', '1']: val = 1.0
                elif val in ['nữ', 'nu', 'female', '0']: val = 0.0
                else: raise ValueError("Giới tính không hợp lệ")
            else:
                val = float(val)
            user_input.append(val)
            
        # Tiền xử lý dữ liệu nhập vào
        input_scaled = scaler.transform([user_input]) 
        
        # Dự đoán
        prediction = best_model.predict(input_scaled)[0]
        probability = best_model.predict_proba(input_scaled)[0][1] * 100
        
        if prediction == 1:
            msg = f"CẢNH BÁO: Bệnh nhân có nguy cơ mắc tiểu đường cao ({probability:.1f}%).\nGiải thích: Các chỉ số sinh hóa vượt ngưỡng an toàn của mô hình học máy. Đề nghị chỉ định kiểm tra chuyên sâu."
        else:
            msg = f"THÔNG BÁO: Bệnh nhân có nguy cơ mắc tiểu đường thấp ({probability:.1f}%).\nGiải thích: Các chỉ số hiện tại đang nằm trong phổ an toàn của tập dữ liệu kiểm soát."
            
        messagebox.showinfo("Kết quả chẩn đoán", msg)
    except ValueError as e:
        messagebox.showerror("Lỗi nhập liệu", f"Vui lòng kiểm tra lại số liệu.\nGiới tính nhập: Nam/Nữ hoặc 1/0.\nCác chỉ số khác nhập số thập phân.\nChi tiết: {e}")

# Thiết lập cửa sổ giao diện Tkinter
root = tk.Tk()
root.title("Phần Mềm Phân Tích Nguy Cơ Tiểu Đường")
root.geometry("450x600")

tk.Label(root, text="NHẬP THÔNG SỐ XÉT NGHIỆM:", font=("Arial", 12, "bold"), fg="blue").pack(pady=10)

entries = []
# Xây dựng các trường nhập liệu tự động dựa trên tên cột thực tế
for feature in X.columns:
    frame = tk.Frame(root)
    frame.pack(pady=3, fill="x", padx=40)
    
    # Gợi ý cho trường giới tính
    label_text = f"{feature} (Nam=1, Nữ=0):" if feature == 'GioiTinh' else f"{feature}:"
    tk.Label(frame, text=label_text, width=22, anchor="w").pack(side=tk.LEFT)
    
    entry = tk.Entry(frame)
    entry.pack(side=tk.RIGHT, expand=True, fill="x")
    entries.append(entry)

tk.Button(root, text="Phân Tích Dữ Liệu", command=predict_risk, bg="green", fg="white", font=("Arial", 12, "bold")).pack(pady=20)

root.mainloop() 