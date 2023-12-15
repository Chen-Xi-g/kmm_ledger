import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

let gradient = LinearGradient(
        colors: [
//            Color.black.opacity(0.5), 透明度0.5
//            Color.black.opacity(0.2), 透明度0.2
            Color.black.opacity(0.0)
        ],
        startPoint: .top, endPoint: .bottom
)

struct ContentView: View {
    var body: some View {
        ZStack {
            ComposeView()
                    .ignoresSafeArea(.all) // Compose has own keyboard handler
            VStack{
                gradient.ignoresSafeArea(edges: .top).frame(height: 0)
                Spacer()
            }
        }
    }
}
