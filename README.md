# FinExpr: an expression evaluator  表达式计算工具

## Brief introduction
FinExpr is an expression evaluator  implemented by Java. Focus on precision, can be used in financial system.

FinExpr是一个Java语言实现的表达式求值工具包。名称Fin是finance的缩写，注重于精度，适用于金融、计费、财务相关对金额精度敏感的系统。在计算时为了避免double类型的数据误差，默认均采用BigDecimal进行计算。 这是一个Maven项目，下载源码后用在IDE选择导入Maven项目即可。
  
## Usage

Expression: org.masking.finexpr.expr.Expression

Simple Example: 简单示例

```Java
Expression e = new Expression("345000*0.0157");
BigDecimal result = e.calculate(); // result 5416.5000
```
  
Custom Function & Add variables: 使用自定义函数 add()、使用4个变量 x, y, a, b

```Java
Expression e = new Expression("add(x,y) + a^b");
	
// define function "add" 自定义函数 add
e.addFunction(new Function("add", 2){
	@Override
	public BigDecimal apply(List<BigDecimal> args, MathContext mc) {
		return args.get(0).add(args.get(1),mc);
	}
});

// set variables,  设置变量的值
e.addVariable("x", new BigDecimal("8.5"));	
e.addVariable("y", new BigDecimal("5.77"));	
e.addVariable("a", new BigDecimal("5"));	
e.addVariable("b", new BigDecimal("3"));	
/*
 *  in this case:
 *  the expression 
 *  = add(8.5,5.77) + 5^3 
 *  = 8.5+5.77 + 5^3 
 *  = 14.27 + 125 
 *  = 139.27
*/
BigDecimal result = e.calculate();
System.out.println(result);

assertTrue(result.equals(new BigDecimal("139.27")));
```
  
Custom Precision & RoundingMode: 自定义精度和舍入模式

```Java
Expression e = new Expression("0.07*2.59", new MathContext(25,RoundingMode.HALF_UP));
```

## 实际应用场景：

例如自定义pmt函数：pmt函数是计算等额本息还款，每期还款金额的公式。

```Java
Expression e = new Expression("pmt(0.1, 12, 10000)");

e.addFunction(new Function("pmt", 3){
	@Override
	public BigDecimal apply(List<BigDecimal> args, MathContext mc) {
	  // implement of pmt();
	  // https://support.office.com/en-us/article/PMT-function-0214da64-9a63-4996-bc20-214433fa6441
	}
});
BigDecimal result = e.calculate();	// result: 计算借款10000元 12期还 年化利率10%，等额本息每期还款金额

// 比如有计费公式是向贷款商户收取每期还款金额的 0.2%作为服务费, 则表达式Expression改成 0.002*pmt(利率, 期数, 本金) 即可
Expression e = new Expression("0.002*pmt(0.1, 12, 10000)");

```

因此，FinExpr特别适用于费用计算、合作商佣金计算等等涉及不同合作方有较高计费规则差异化定制的需求场景
 
  
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

我在公司做计费相关模块时，有这样的需求，对于一笔贷款，在贷款的生命周期的各个阶段都需要收取一定的手续费/服务费/保证金等费用。
而对于来自不同商户、不同类型的贷款收费规则也差异较大。比如保证金计算公式 pv\*0.0157 （pv是贷款本金，0.0157是保证金比例），比如每期服务费公式0.01\*PMT(rate, n, pv, 0, false) （PMT是金融相关的函数，Excel里也内置了该公式）。 所以我们就需要一个让业务人员可以自定义编辑计费表达式的系统(在这个系统之前是通过Excel公式批量手工处理的)。  一开始公司代码库里有个用Spring EL实现的表达式计算公共Jar包。所以这个表达式计算需求就使用这个现成的Jar包实现了。

直到一次测试时，发现一笔保证金少收了1分钱，当时一笔贷款金额为 3450元，保证金计算公式是 pv\*0.0157 很简单。然而 3450\*0.0157实际应该等于 54.165元，业务人员规定计算结果按四舍五入精确到分，应收54.17元保证金。 然而在系统里 3450\*0.0157=54.16499999999999  当四舍五入精确到分时则变成了54.16元。

当然如果是简单的 pv\*0.0157这样的乘法，那么很好解决，把公式换成 pv\*157/10000.0,  或者把参与计算的数值都换成BigDecimal就可以了。但是业务的需求需要配置几百个甚至数千个不同的复杂的公式，还包括对pmt、ipmt、ppmt等金融公式和自定义函数。而Spring EL并不支持BigDecimal， 并且在表达式里的字面常量的精度是最小满足的， 比如如果公式里包含  3/10，那在Spring El里它的表示的值是0，而不是0.3，因为都是整数，这对于那些不是计算机相关专业的负责配置计费公式的业务人员来说简直是灾难。

因为排期问题，首先选择的临时解决方案是在配公式时注意参与计算的小数 比如 0.0157 都写成 157/10000.0 。当然这个方案很容易出错，不是长久之计。
后续准备选择更换表达式计算引擎，选择支持BigDecimal的框架。
然而调研了十几个主流的表达式求值工具，均不能完全满足需求。
比如 Ognl、MVE、JSEL 这些类脚本语言，以及 exp4j、expr4j、Aviator等等。
这些工具使用BigDecimal的话需要在表达式里对每个数值手动标识,不能兼容系统已有的几百个表达式，
对于自定义函数支持也不方便，需要在表达式里写成JavaClass.method()或javaObject.method(), 这需要对系统里历史的所有公式按新框架要求修改，而且对于配公式的业务人员来说这样的方式也比较怪异，他们习惯的是和Excel里一样的公式使用方式。  

后来发现一款优秀的表达式计算工具 **[EvalEx](https://github.com/uklimaschewski/EvalEx)** 这个工具计算全程采用BigDecimal, 对于表达式里的字面量比如 35.6\*12.3 会自动识别构造成BigDecimal去计算。对于用户自定义的变量参数比如 3\*var ， var可以需要传入一个BigDecimal变量。而且可以很方便的自定义函数，从而实现了和在Excel里计算表达式一样的简捷功能， 比如通过自定义加入pmt公式， 可以直接计算表达式"pmt(rate, n, pv)"。

但是EvalEx也有些许小小的缺陷，比如为了追求“handy”，EvalEx所有类都作为内部类放在一个Java文件里。EvalEx自定义函数时 Function类不是静态类，是内部非静态类，导致每次创建不同的公式都需要新建匿名类，在并发较高时可能会产生性能问题。 而作者为了兼容已有系统 不打算接受更改。EvalEx对一元操作符支持也有问题（最新版1.9已修改）等等。于是重新造了一个轮子 FinExpr。
