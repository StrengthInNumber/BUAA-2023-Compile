; ModuleID = 'llvm-link'
source_filename = "llvm-link"
target datalayout = "e-m:o-i64:64-i128:128-n32:64-S128"
target triple = "arm64-apple-macosx14.0.0"

@Global_0 = dso_local global [5 x i32] zeroinitializer
@Global_1 = dso_local global [25 x i32] zeroinitializer
@.str = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@.str.1 = private unnamed_addr constant [3 x i8] c"%c\00", align 1
@.str.2 = private unnamed_addr constant [4 x i8] c"%d:\00", align 1
@.str.3 = private unnamed_addr constant [4 x i8] c" %d\00", align 1
@.str.4 = private unnamed_addr constant [2 x i8] c"\0A\00", align 1
@.str.5 = private unnamed_addr constant [3 x i8] c"%s\00", align 1

define dso_local i32 @f_copyArray1D(ptr %FParam_0) {
BB_0:
  br label %BB_1

BB_1:                                             ; preds = %BB_3, %BB_0
  %Local_193 = phi i32 [ 0, %BB_0 ], [ %Local_11, %BB_3 ]
  %Local_2 = icmp slt i32 %Local_193, 5
  %Local_3 = zext i1 %Local_2 to i32
  %Local_4 = icmp ne i32 %Local_3, 0
  br i1 %Local_4, label %BB_2, label %BB_4

BB_2:                                             ; preds = %BB_1
  %Local_6 = getelementptr inbounds [5 x i32], ptr @Global_0, i32 0, i32 %Local_193
  %Local_7 = getelementptr inbounds i32, ptr %FParam_0, i32 %Local_193
  %Local_9 = load i32, ptr %Local_7, align 4
  store i32 %Local_9, ptr %Local_6, align 4
  br label %BB_3

BB_3:                                             ; preds = %BB_2
  %Local_11 = add i32 %Local_193, 1
  br label %BB_1

BB_4:                                             ; preds = %BB_1
  ret i32 1
}

define dso_local i32 @f_copyArray2D(ptr %FParam_1) {
BB_5:
  br label %BB_6

BB_6:                                             ; preds = %BB_8, %BB_5
  %Local_194 = phi i32 [ 0, %BB_5 ], [ %Local_36, %BB_8 ]
  %Local_15 = icmp slt i32 %Local_194, 5
  %Local_16 = zext i1 %Local_15 to i32
  %Local_17 = icmp ne i32 %Local_16, 0
  br i1 %Local_17, label %BB_7, label %BB_9

BB_7:                                             ; preds = %BB_6
  br label %BB_10

BB_8:                                             ; preds = %BB_13
  %Local_36 = add i32 %Local_194, 1
  br label %BB_6

BB_9:                                             ; preds = %BB_6
  ret i32 1

BB_10:                                            ; preds = %BB_12, %BB_7
  %Local_195 = phi i32 [ 0, %BB_7 ], [ %Local_34, %BB_12 ]
  %Local_19 = icmp slt i32 %Local_195, 5
  %Local_20 = zext i1 %Local_19 to i32
  %Local_21 = icmp ne i32 %Local_20, 0
  br i1 %Local_21, label %BB_11, label %BB_13

BB_11:                                            ; preds = %BB_10
  %Local_24 = mul i32 %Local_194, 5
  %Local_25 = add i32 %Local_195, %Local_24
  %Local_26 = getelementptr inbounds [25 x i32], ptr @Global_1, i32 0, i32 %Local_25
  %Local_27 = mul i32 5, %Local_194
  %Local_29 = add i32 %Local_27, %Local_195
  %Local_31 = getelementptr inbounds i32, ptr %FParam_1, i32 %Local_29
  %Local_32 = load i32, ptr %Local_31, align 4
  store i32 %Local_32, ptr %Local_26, align 4
  br label %BB_12

BB_12:                                            ; preds = %BB_11
  %Local_34 = add i32 %Local_195, 1
  br label %BB_10

BB_13:                                            ; preds = %BB_10
  br label %BB_8
}

define dso_local void @f_copy1DInto2D(ptr %FParam_2, i32 %FParam_3) {
BB_14:
  br label %BB_15

BB_15:                                            ; preds = %BB_17, %BB_14
  %Local_197 = phi i32 [ 0, %BB_14 ], [ %Local_52, %BB_17 ]
  %Local_40 = icmp slt i32 %Local_197, 5
  %Local_41 = zext i1 %Local_40 to i32
  %Local_42 = icmp ne i32 %Local_41, 0
  br i1 %Local_42, label %BB_16, label %BB_18

BB_16:                                            ; preds = %BB_15
  %Local_45 = mul i32 %FParam_3, 5
  %Local_46 = add i32 %Local_197, %Local_45
  %Local_47 = getelementptr inbounds [25 x i32], ptr @Global_1, i32 0, i32 %Local_46
  %Local_48 = getelementptr inbounds i32, ptr %FParam_2, i32 %Local_197
  %Local_50 = load i32, ptr %Local_48, align 4
  store i32 %Local_50, ptr %Local_47, align 4
  br label %BB_17

BB_17:                                            ; preds = %BB_16
  %Local_52 = add i32 %Local_197, 1
  br label %BB_15

BB_18:                                            ; preds = %BB_15
  ret void
}

define dso_local i32 @main() {
BB_19:
  call void @putch(i32 50)
  call void @putch(i32 49)
  call void @putch(i32 51)
  call void @putch(i32 55)
  call void @putch(i32 51)
  call void @putch(i32 52)
  call void @putch(i32 53)
  call void @putch(i32 55)
  call void @putch(i32 10)
  %Local_55 = alloca [5 x i32], align 4
  %Local_56 = getelementptr inbounds [5 x i32], ptr %Local_55, i32 0, i32 0
  store i32 0, ptr %Local_56, align 4
  %Local_57 = getelementptr inbounds [5 x i32], ptr %Local_55, i32 0, i32 1
  store i32 1, ptr %Local_57, align 4
  %Local_58 = getelementptr inbounds [5 x i32], ptr %Local_55, i32 0, i32 2
  store i32 2, ptr %Local_58, align 4
  %Local_59 = getelementptr inbounds [5 x i32], ptr %Local_55, i32 0, i32 3
  store i32 3, ptr %Local_59, align 4
  %Local_60 = getelementptr inbounds [5 x i32], ptr %Local_55, i32 0, i32 4
  store i32 4, ptr %Local_60, align 4
  %Local_61 = alloca [25 x i32], align 4
  %Local_62 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 0
  store i32 0, ptr %Local_62, align 4
  %Local_63 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 1
  store i32 1, ptr %Local_63, align 4
  %Local_64 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 2
  store i32 2, ptr %Local_64, align 4
  %Local_65 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 3
  store i32 3, ptr %Local_65, align 4
  %Local_66 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 4
  store i32 4, ptr %Local_66, align 4
  %Local_67 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 5
  store i32 10, ptr %Local_67, align 4
  %Local_68 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 6
  store i32 11, ptr %Local_68, align 4
  %Local_69 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 7
  store i32 12, ptr %Local_69, align 4
  %Local_70 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 8
  store i32 13, ptr %Local_70, align 4
  %Local_71 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 9
  store i32 14, ptr %Local_71, align 4
  %Local_72 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 10
  store i32 20, ptr %Local_72, align 4
  %Local_73 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 11
  store i32 21, ptr %Local_73, align 4
  %Local_74 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 12
  store i32 22, ptr %Local_74, align 4
  %Local_75 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 13
  store i32 23, ptr %Local_75, align 4
  %Local_76 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 14
  store i32 24, ptr %Local_76, align 4
  %Local_77 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 15
  store i32 30, ptr %Local_77, align 4
  %Local_78 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 16
  store i32 31, ptr %Local_78, align 4
  %Local_79 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 17
  store i32 32, ptr %Local_79, align 4
  %Local_80 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 18
  store i32 33, ptr %Local_80, align 4
  %Local_81 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 19
  store i32 34, ptr %Local_81, align 4
  %Local_82 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 20
  store i32 40, ptr %Local_82, align 4
  %Local_83 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 21
  store i32 41, ptr %Local_83, align 4
  %Local_84 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 22
  store i32 42, ptr %Local_84, align 4
  %Local_85 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 23
  store i32 43, ptr %Local_85, align 4
  %Local_86 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 24
  store i32 44, ptr %Local_86, align 4
  %Local_88 = getelementptr inbounds [5 x i32], ptr %Local_55, i32 0, i32 0
  %Local_87 = call i32 @f_copyArray1D(ptr %Local_88)
  %Local_89 = icmp ne i32 %Local_87, 0
  br i1 %Local_89, label %BB_20, label %BB_21

BB_20:                                            ; preds = %BB_19
  br label %BB_22

BB_21:                                            ; preds = %BB_29, %BB_19
  br label %BB_34

BB_22:                                            ; preds = %BB_24, %BB_20
  %Local_201 = phi i32 [ 0, %BB_20 ], [ %Local_100, %BB_24 ]
  %Local_91 = icmp slt i32 %Local_201, 5
  %Local_92 = zext i1 %Local_91 to i32
  %Local_93 = icmp ne i32 %Local_92, 0
  br i1 %Local_93, label %BB_23, label %BB_25

BB_23:                                            ; preds = %BB_22
  %Local_94 = getelementptr inbounds [5 x i32], ptr @Global_0, i32 0, i32 %Local_201
  %Local_96 = load i32, ptr %Local_94, align 4
  %Local_98 = icmp ne i32 %Local_96, %Local_201
  br i1 %Local_98, label %BB_26, label %BB_27

BB_24:                                            ; preds = %BB_27
  %Local_100 = add i32 %Local_201, 1
  br label %BB_22

BB_25:                                            ; preds = %BB_26, %BB_22
  %Local_102 = icmp eq i32 %Local_201, 5
  br i1 %Local_102, label %BB_28, label %BB_29

BB_26:                                            ; preds = %BB_23
  br label %BB_25

BB_27:                                            ; preds = %BB_23
  br label %BB_24

BB_28:                                            ; preds = %BB_25
  call void @putch(i32 49)
  call void @putch(i32 68)
  call void @putch(i32 32)
  call void @putch(i32 97)
  call void @putch(i32 114)
  call void @putch(i32 114)
  call void @putch(i32 97)
  call void @putch(i32 121)
  call void @putch(i32 32)
  call void @putch(i32 102)
  call void @putch(i32 117)
  call void @putch(i32 110)
  call void @putch(i32 99)
  call void @putch(i32 116)
  call void @putch(i32 105)
  call void @putch(i32 111)
  call void @putch(i32 110)
  call void @putch(i32 32)
  call void @putch(i32 112)
  call void @putch(i32 97)
  call void @putch(i32 115)
  call void @putch(i32 115)
  call void @putch(i32 33)
  call void @putch(i32 10)
  br label %BB_30

BB_29:                                            ; preds = %BB_33, %BB_25
  br label %BB_21

BB_30:                                            ; preds = %BB_32, %BB_28
  %Local_208 = phi i32 [ 1, %BB_28 ], [ %Local_109, %BB_32 ]
  %Local_105 = icmp slt i32 %Local_208, 1000
  %Local_106 = zext i1 %Local_105 to i32
  %Local_107 = icmp ne i32 %Local_106, 0
  br i1 %Local_107, label %BB_31, label %BB_33

BB_31:                                            ; preds = %BB_30
  %Local_109 = mul i32 %Local_208, 2
  br label %BB_32

BB_32:                                            ; preds = %BB_31
  br label %BB_30

BB_33:                                            ; preds = %BB_30
  call void @putint(i32 %Local_208)
  call void @putch(i32 10)
  br label %BB_29

BB_34:                                            ; preds = %BB_36, %BB_21
  %Local_207 = phi i32 [ 1, %BB_21 ], [ %Local_117, %BB_36 ]
  %Local_113 = icmp slt i32 %Local_207, 1000
  %Local_114 = zext i1 %Local_113 to i32
  %Local_115 = icmp ne i32 %Local_114, 0
  br i1 %Local_115, label %BB_35, label %BB_37

BB_35:                                            ; preds = %BB_34
  %Local_117 = mul i32 %Local_207, 2
  br label %BB_36

BB_36:                                            ; preds = %BB_35
  br label %BB_34

BB_37:                                            ; preds = %BB_34
  call void @putint(i32 %Local_207)
  call void @putch(i32 10)
  br label %BB_38

BB_38:                                            ; preds = %BB_40, %BB_37
  %Local_211 = phi i32 [ 1, %BB_37 ], [ %Local_125, %BB_40 ]
  %Local_121 = icmp slt i32 %Local_211, 1000
  %Local_122 = zext i1 %Local_121 to i32
  %Local_123 = icmp ne i32 %Local_122, 0
  br i1 %Local_123, label %BB_39, label %BB_41

BB_39:                                            ; preds = %BB_38
  %Local_125 = mul i32 %Local_211, 2
  br label %BB_40

BB_40:                                            ; preds = %BB_39
  br label %BB_38

BB_41:                                            ; preds = %BB_38
  call void @putint(i32 %Local_211)
  call void @putch(i32 10)
  %Local_128 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 0
  %Local_127 = call i32 @f_copyArray2D(ptr %Local_128)
  %Local_129 = icmp ne i32 %Local_127, 0
  br i1 %Local_129, label %BB_42, label %BB_43

BB_42:                                            ; preds = %BB_41
  br label %BB_44

BB_43:                                            ; preds = %BB_57, %BB_41
  br label %BB_60

BB_44:                                            ; preds = %BB_46, %BB_42
  %Local_204 = phi i32 [ 114514, %BB_42 ], [ %Local_203, %BB_46 ]
  %Local_199 = phi i32 [ 0, %BB_42 ], [ %Local_154, %BB_46 ]
  %Local_131 = icmp slt i32 %Local_199, 5
  %Local_132 = zext i1 %Local_131 to i32
  %Local_133 = icmp ne i32 %Local_132, 0
  br i1 %Local_133, label %BB_45, label %BB_47

BB_45:                                            ; preds = %BB_44
  br label %BB_48

BB_46:                                            ; preds = %BB_55
  %Local_154 = add i32 %Local_199, 1
  br label %BB_44

BB_47:                                            ; preds = %BB_54, %BB_44
  %Local_205 = phi i32 [ %Local_204, %BB_44 ], [ %Local_203, %BB_54 ]
  %Local_156 = icmp eq i32 %Local_199, 5
  br i1 %Local_156, label %BB_56, label %BB_57

BB_48:                                            ; preds = %BB_50, %BB_45
  %Local_203 = phi i32 [ 0, %BB_45 ], [ %Local_150, %BB_50 ]
  %Local_135 = icmp slt i32 %Local_203, 5
  %Local_136 = zext i1 %Local_135 to i32
  %Local_137 = icmp ne i32 %Local_136, 0
  br i1 %Local_137, label %BB_49, label %BB_51

BB_49:                                            ; preds = %BB_48
  %Local_138 = mul i32 5, %Local_199
  %Local_140 = add i32 %Local_138, %Local_203
  %Local_142 = getelementptr inbounds [25 x i32], ptr @Global_1, i32 0, i32 %Local_140
  %Local_143 = load i32, ptr %Local_142, align 4
  %Local_145 = mul i32 %Local_199, 10
  %Local_147 = add i32 %Local_145, %Local_203
  %Local_148 = icmp ne i32 %Local_143, %Local_147
  br i1 %Local_148, label %BB_52, label %BB_53

BB_50:                                            ; preds = %BB_53
  %Local_150 = add i32 %Local_203, 1
  br label %BB_48

BB_51:                                            ; preds = %BB_52, %BB_48
  %Local_152 = icmp ne i32 %Local_203, 5
  br i1 %Local_152, label %BB_54, label %BB_55

BB_52:                                            ; preds = %BB_49
  br label %BB_51

BB_53:                                            ; preds = %BB_49
  br label %BB_50

BB_54:                                            ; preds = %BB_51
  br label %BB_47

BB_55:                                            ; preds = %BB_51
  br label %BB_46

BB_56:                                            ; preds = %BB_47
  %Local_158 = icmp eq i32 %Local_205, 5
  br i1 %Local_158, label %BB_58, label %BB_59

BB_57:                                            ; preds = %BB_59, %BB_47
  br label %BB_43

BB_58:                                            ; preds = %BB_56
  call void @putch(i32 50)
  call void @putch(i32 68)
  call void @putch(i32 32)
  call void @putch(i32 97)
  call void @putch(i32 114)
  call void @putch(i32 114)
  call void @putch(i32 97)
  call void @putch(i32 121)
  call void @putch(i32 32)
  call void @putch(i32 102)
  call void @putch(i32 117)
  call void @putch(i32 110)
  call void @putch(i32 99)
  call void @putch(i32 116)
  call void @putch(i32 105)
  call void @putch(i32 111)
  call void @putch(i32 110)
  call void @putch(i32 32)
  call void @putch(i32 112)
  call void @putch(i32 97)
  call void @putch(i32 115)
  call void @putch(i32 115)
  call void @putch(i32 33)
  call void @putch(i32 10)
  br label %BB_59

BB_59:                                            ; preds = %BB_58, %BB_56
  br label %BB_57

BB_60:                                            ; preds = %BB_62, %BB_43
  %Local_212 = phi i32 [ 1, %BB_43 ], [ %Local_165, %BB_62 ]
  %Local_161 = icmp slt i32 %Local_212, 1000
  %Local_162 = zext i1 %Local_161 to i32
  %Local_163 = icmp ne i32 %Local_162, 0
  br i1 %Local_163, label %BB_61, label %BB_63

BB_61:                                            ; preds = %BB_60
  %Local_165 = mul i32 %Local_212, 2
  br label %BB_62

BB_62:                                            ; preds = %BB_61
  br label %BB_60

BB_63:                                            ; preds = %BB_60
  call void @putint(i32 %Local_212)
  call void @putch(i32 10)
  %Local_168 = mul i32 5, 0
  %Local_169 = getelementptr inbounds [25 x i32], ptr %Local_61, i32 0, i32 %Local_168
  call void @f_copy1DInto2D(ptr %Local_169, i32 1)
  br label %BB_64

BB_64:                                            ; preds = %BB_66, %BB_63
  %Local_198 = phi i32 [ 0, %BB_63 ], [ %Local_182, %BB_66 ]
  %Local_171 = icmp slt i32 %Local_198, 5
  %Local_172 = zext i1 %Local_171 to i32
  %Local_173 = icmp ne i32 %Local_172, 0
  br i1 %Local_173, label %BB_65, label %BB_67

BB_65:                                            ; preds = %BB_64
  %Local_174 = mul i32 5, 1
  %Local_175 = add i32 %Local_174, %Local_198
  %Local_177 = getelementptr inbounds [25 x i32], ptr @Global_1, i32 0, i32 %Local_175
  %Local_178 = load i32, ptr %Local_177, align 4
  %Local_180 = icmp ne i32 %Local_178, %Local_198
  br i1 %Local_180, label %BB_68, label %BB_69

BB_66:                                            ; preds = %BB_69
  %Local_182 = add i32 %Local_198, 1
  br label %BB_64

BB_67:                                            ; preds = %BB_68, %BB_64
  br label %BB_70

BB_68:                                            ; preds = %BB_65
  br label %BB_67

BB_69:                                            ; preds = %BB_65
  br label %BB_66

BB_70:                                            ; preds = %BB_72, %BB_67
  %Local_213 = phi i32 [ 1, %BB_67 ], [ %Local_189, %BB_72 ]
  %Local_185 = icmp slt i32 %Local_213, 1000
  %Local_186 = zext i1 %Local_185 to i32
  %Local_187 = icmp ne i32 %Local_186, 0
  br i1 %Local_187, label %BB_71, label %BB_73

BB_71:                                            ; preds = %BB_70
  %Local_189 = mul i32 %Local_213, 2
  br label %BB_72

BB_72:                                            ; preds = %BB_71
  br label %BB_70

BB_73:                                            ; preds = %BB_70
  call void @putint(i32 %Local_213)
  call void @putch(i32 10)
  %Local_192 = icmp eq i32 %Local_198, 5
  br i1 %Local_192, label %BB_74, label %BB_75

BB_74:                                            ; preds = %BB_73
  call void @putch(i32 49)
  call void @putch(i32 68)
  call void @putch(i32 32)
  call void @putch(i32 97)
  call void @putch(i32 114)
  call void @putch(i32 114)
  call void @putch(i32 97)
  call void @putch(i32 121)
  call void @putch(i32 32)
  call void @putch(i32 105)
  call void @putch(i32 110)
  call void @putch(i32 32)
  call void @putch(i32 50)
  call void @putch(i32 68)
  call void @putch(i32 32)
  call void @putch(i32 97)
  call void @putch(i32 114)
  call void @putch(i32 114)
  call void @putch(i32 97)
  call void @putch(i32 121)
  call void @putch(i32 32)
  call void @putch(i32 102)
  call void @putch(i32 117)
  call void @putch(i32 110)
  call void @putch(i32 99)
  call void @putch(i32 116)
  call void @putch(i32 105)
  call void @putch(i32 111)
  call void @putch(i32 110)
  call void @putch(i32 32)
  call void @putch(i32 112)
  call void @putch(i32 97)
  call void @putch(i32 115)
  call void @putch(i32 115)
  call void @putch(i32 33)
  call void @putch(i32 10)
  br label %BB_75

BB_75:                                            ; preds = %BB_74, %BB_73
  call void @putch(i32 84)
  call void @putch(i32 101)
  call void @putch(i32 115)
  call void @putch(i32 116)
  call void @putch(i32 50)
  call void @putch(i32 32)
  call void @putch(i32 83)
  call void @putch(i32 117)
  call void @putch(i32 99)
  call void @putch(i32 99)
  call void @putch(i32 101)
  call void @putch(i32 115)
  call void @putch(i32 115)
  call void @putch(i32 33)
  ret i32 0
}

; Function Attrs: noinline nounwind optnone ssp uwtable(sync)
define i32 @getint() #0 {
  %1 = alloca i32, align 4
  %2 = call i32 (ptr, ...) @scanf(ptr noundef @.str, ptr noundef %1)
  %3 = load i32, ptr %1, align 4
  ret i32 %3
}

declare i32 @scanf(ptr noundef, ...) #1

; Function Attrs: noinline nounwind optnone ssp uwtable(sync)
define i32 @getch() #0 {
  %1 = alloca i8, align 1
  %2 = call i32 (ptr, ...) @scanf(ptr noundef @.str.1, ptr noundef %1)
  %3 = load i8, ptr %1, align 1
  %4 = sext i8 %3 to i32
  ret i32 %4
}

; Function Attrs: noinline nounwind optnone ssp uwtable(sync)
define i32 @getarray(ptr noundef %0) #0 {
  %2 = alloca ptr, align 8
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  store ptr %0, ptr %2, align 8
  %5 = call i32 (ptr, ...) @scanf(ptr noundef @.str, ptr noundef %3)
  store i32 0, ptr %4, align 4
  br label %6

6:                                                ; preds = %16, %1
  %7 = load i32, ptr %4, align 4
  %8 = load i32, ptr %3, align 4
  %9 = icmp slt i32 %7, %8
  br i1 %9, label %10, label %19

10:                                               ; preds = %6
  %11 = load ptr, ptr %2, align 8
  %12 = load i32, ptr %4, align 4
  %13 = sext i32 %12 to i64
  %14 = getelementptr inbounds i32, ptr %11, i64 %13
  %15 = call i32 (ptr, ...) @scanf(ptr noundef @.str, ptr noundef %14)
  br label %16

16:                                               ; preds = %10
  %17 = load i32, ptr %4, align 4
  %18 = add nsw i32 %17, 1
  store i32 %18, ptr %4, align 4
  br label %6, !llvm.loop !5

19:                                               ; preds = %6
  %20 = load i32, ptr %3, align 4
  ret i32 %20
}

; Function Attrs: noinline nounwind optnone ssp uwtable(sync)
define void @putint(i32 noundef %0) #0 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
  %3 = load i32, ptr %2, align 4
  %4 = call i32 (ptr, ...) @printf(ptr noundef @.str, i32 noundef %3)
  ret void
}

declare i32 @printf(ptr noundef, ...) #1

; Function Attrs: noinline nounwind optnone ssp uwtable(sync)
define void @putch(i32 noundef %0) #0 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
  %3 = load i32, ptr %2, align 4
  %4 = call i32 (ptr, ...) @printf(ptr noundef @.str.1, i32 noundef %3)
  ret void
}

; Function Attrs: noinline nounwind optnone ssp uwtable(sync)
define void @putarray(i32 noundef %0, ptr noundef %1) #0 {
  %3 = alloca i32, align 4
  %4 = alloca ptr, align 8
  %5 = alloca i32, align 4
  store i32 %0, ptr %3, align 4
  store ptr %1, ptr %4, align 8
  %6 = load i32, ptr %3, align 4
  %7 = call i32 (ptr, ...) @printf(ptr noundef @.str.2, i32 noundef %6)
  store i32 0, ptr %5, align 4
  br label %8

8:                                                ; preds = %19, %2
  %9 = load i32, ptr %5, align 4
  %10 = load i32, ptr %3, align 4
  %11 = icmp slt i32 %9, %10
  br i1 %11, label %12, label %22

12:                                               ; preds = %8
  %13 = load ptr, ptr %4, align 8
  %14 = load i32, ptr %5, align 4
  %15 = sext i32 %14 to i64
  %16 = getelementptr inbounds i32, ptr %13, i64 %15
  %17 = load i32, ptr %16, align 4
  %18 = call i32 (ptr, ...) @printf(ptr noundef @.str.3, i32 noundef %17)
  br label %19

19:                                               ; preds = %12
  %20 = load i32, ptr %5, align 4
  %21 = add nsw i32 %20, 1
  store i32 %21, ptr %5, align 4
  br label %8, !llvm.loop !7

22:                                               ; preds = %8
  %23 = call i32 (ptr, ...) @printf(ptr noundef @.str.4)
  ret void
}

; Function Attrs: noinline nounwind optnone ssp uwtable(sync)
define void @putstr(ptr noundef %0) #0 {
  %2 = alloca ptr, align 8
  store ptr %0, ptr %2, align 8
  %3 = load ptr, ptr %2, align 8
  %4 = call i32 (ptr, ...) @printf(ptr noundef @.str.5, ptr noundef %3)
  ret void
}

attributes #0 = { noinline nounwind optnone ssp uwtable(sync) "frame-pointer"="non-leaf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="apple-m1" "target-features"="+aes,+crc,+dotprod,+fp-armv8,+fp16fml,+fullfp16,+lse,+neon,+ras,+rcpc,+rdm,+sha2,+sha3,+v8.1a,+v8.2a,+v8.3a,+v8.4a,+v8.5a,+v8a,+zcm,+zcz" }
attributes #1 = { "frame-pointer"="non-leaf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="apple-m1" "target-features"="+aes,+crc,+dotprod,+fp-armv8,+fp16fml,+fullfp16,+lse,+neon,+ras,+rcpc,+rdm,+sha2,+sha3,+v8.1a,+v8.2a,+v8.3a,+v8.4a,+v8.5a,+v8a,+zcm,+zcz" }

!llvm.ident = !{!0}
!llvm.module.flags = !{!1, !2, !3, !4}

!0 = !{!"Homebrew clang version 17.0.1"}
!1 = !{i32 1, !"wchar_size", i32 4}
!2 = !{i32 8, !"PIC Level", i32 2}
!3 = !{i32 7, !"uwtable", i32 1}
!4 = !{i32 7, !"frame-pointer", i32 1}
!5 = distinct !{!5, !6}
!6 = !{!"llvm.loop.mustprogress"}
!7 = distinct !{!7, !6}
