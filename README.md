# FinExpr: an expression evaluator  表达式计算工具

## Brief introduction
FinExpr is an expression evaluator  implemented by Java. Focus on precision, can be used in financial system.

FinExpr是一个Java语言实现的表达式求值工具包。名称Fin是finance的缩写，注重于精度，适用于金融、计费、财务相关对金额精度敏感的系统。在计算时为了避免double类型的数据误差，默认均采用BigDecimal进行计算。
  
  
## Usage

Expression: org.masking.finexpr.expr.Expression

Simple Example: 简单示例

```Java
Expression e = new Expression("345000*0.0157");
BigDecimal result = e.calculate(); // result 5416.5000
```
  
Custom Function & Add variables: 使用自定义函数 fx()、使用变量 x

```Java
Expression e = new Expression("fx(9, 7.3, x)");
e.addFunction(new Function("fx", 3){
	@Override
	public BigDecimal apply(MathContext mc, List<BigDecimal> args) {
		return args.get(0).add(args.get(1),mc).subtract(args.get(2),mc);
	}
});
e.addVariable("x", new BigDecimal("8.5"));	
BigDecimal result = e.calculate();		// result: 7.8
```
  
Custom Precision & RoundingMode: 自定义精度和舍入模式

```Java
Expression e = new Expression("0.07*2.59", new MathContext(25,RoundingMode.HALF_UP));
```
  
  
## Default Supported Operators

| Operator        | Description           | 
| ------------- |:-------------:| 
| +   | Additive operator / Unary plus | 
| -    | Subtraction operator / Unary minus|   
| *    | Multiplication operator    |    
| /    | Division operator       |    
| ^    | Power operator          |    
  
you can add custom operators by addOperator();   
  
Tips: currently the symbol of operator can only be one character.
  

## Background

我在公司(一家互联网金融公司)做资产平台计费模块时，有这样的需求，贷款Loan是由公司合作的商户(贷款公司)进件过来的，对于一笔贷款Loan，在Loan的生命周期的各个阶段都需要收取一定的手续费/服务费/保证金等费用。比如审核通过时向商户收取保证金，放款成功时收取服务费。 而合作的商户很多，不同的商户每项费用的计算公式都不一样。即使是同一个商户，对于不同期数不同资产类目的贷款，收费公式也不尽相同。 所以我们就需要一个让业务人员可以自由编辑计费表达式，比如保证金计算公式 pv\*0.0157 （pv是贷款本金，0.0157是保证金比例），比如每期服务费公式0.01\*PMT(rate, n, pv, 0, false) （PMT是金融相关的函数，excel也内置了）。一开始公司代码库里有个用Spring EL实现的表达式计算公共Jar包。所以这个表达式计算需求就使用这个现成的Jar包实现了。

直到一次测试时，发现一笔保证金少收了1分钱，当时一笔贷款金额为 3450元，保证金计算公式是 pv\*0.0157 很简单。然而 3450\*0.0157实际应该等于 54.165元，业务人员规定计算结果按四舍五入精确到分，应收54.17元保证金。 然而在系统里 3450\*0.0157=54.16499999999999  当四舍五入精确到分时则变成了54.16元。

当然如果是简单的 pv\*0.0157这样的乘法，那么很好解决，把公式换成 pv\*157/10000.0,  或者把参与计算的数值都换成BigDecimal就可以了。但是业务的需求需要配置几百个甚至数千个不同的复杂的公式，还包括对pmt、ipmt、ppmt等金融公式和自定义函数。而Spring EL并不支持BigDecimal， 并且在表达式里的字面常量的精度是最小满足的， 比如如果公式里包含  3/10，那在Spring El里它的表示的值是0，而不是0.3，因为都是整数，这对于那些不是计算机相关专业的负责配置计费公式的业务人员来说简直是灾难。

因为排期问题，首先选择的临时解决方案是在配公式时注意参与计算的小数 比如 0.0157 都写成 157/10000.0 。当然这个方案很容易出错，不是长久之计。
后续准备选择更换表达式计算引擎，选择支持BigDecimal的框架。
然而调研了十几个主流的表达式求值工具，均不能完全满足需求。
比如 Ognl、MVE、JSEL 这些类脚本语言，以及 exp4j、expr4j、Aviator等等。
要么对于自定义函数支持的不方便，需要在表达式里写成JavaClass.method()或javaObject.method(), 这就需要对系统里历史的所有公式按新框架要求修改， 而且对于配公式的业务人员来说这样的方式也比较怪异，他们习惯的是和Excel里一样的表达式。
后来发现一款优秀的表达式计算工具 **[EvalEx](https://github.com/uklimaschewski/EvalEx)** 这个工具计算全程采用BigDecimal, 对于表达式里的字面量比如 35.6\*12.3 会自动识别构造成BigDecimal去计算。对于用户自定义的变量参数比如 3\*var ， var可以需要传入一个BigDecimal变量。而且可以很方便的自定义函数，从而实现了和在Excel里计算表达式一样的简捷功能， 比如通过自定义加入pmt公式， 可以直接计算表达式"pmt(rate, n, pv)"。

但是EvalEx也有些许小小的缺陷，比如为了追求“handy”，EvalEx所有类都作为内部类放在一个Java文件里。自定义函数时 Function类不是静态类，每次不同的公式都需要重写new Function， 而作者为了兼容已有系统 不打算接受更改。对一元操作符支持有问题（最新版已修改）等等。于是重新造了一个轮子 FinExpr。
  
My Blog: http://imasking.me