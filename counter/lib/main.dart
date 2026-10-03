import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_reactions/flutter_reactions.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(primarySwatch: Colors.blue),
      routes: {
        '/': (context) => const MyHomePage(title: 'Flutter Demo Home Page'),
        '/home': (context) => const HomePage(),
      },
    );
  }
}

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(
        child: Text('HomePage'),
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  String? _nativeMessage;
  FlutterReactionType? _flutterReactionType;

  static const channel = MethodChannel('com.animations/flutter');

  @override
  void initState() {
    super.initState();
    channel.setMethodCallHandler(_handleNativeCall);
  }

  Future<dynamic> _handleNativeCall(MethodCall call) async {
    switch (call.method) {
      case 'updateFromNative':
        final data = call.arguments as String?;
        if (mounted) {
          setState(() {
            _nativeMessage = data;
          });
        }
        return 'received';
      default:
        throw MissingPluginException('Not implemented: ${call.method}');
    }
  }

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  Future<void> sendData() async {
    await channel.invokeMethod(
      'sendDataToNative',
      {'counter': 'Hello from Flutter, counter: $_counter\nreaction:${_flutterReactionType?.label}'},
    );
    if (mounted) {
      SystemNavigator.pop();
    }
  }

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) {
        if (!didPop) {
          sendData();
        }
      },
      child: Scaffold(
        appBar: AppBar(title: Text(widget.title)),
        body: Center(
          child: Column(
            spacing: 16.0,
            mainAxisAlignment: .center,
            children: [
              const Text('You have pushed the button this many times'),
              InkWell(
                onTap: sendData,
                child: Text(
                  '$_counter',
                  style: Theme.of(context).textTheme.headlineMedium,
                ),
              ),
              FlutterReactionButton(
                value: _flutterReactionType,
                onChanged: (value) {
                  setState(() {
                    _flutterReactionType = value;
                  });
                },
              ),
              if (_nativeMessage != null) ...[
                Text('From native: $_nativeMessage'),
              ],
            ],
          ),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: _incrementCounter,
          tooltip: 'Increment',
          child: const Icon(Icons.add),
        ),
      ),
    );
  }
}
