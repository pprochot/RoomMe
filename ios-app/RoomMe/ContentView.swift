//
//  ContentView.swift
//  RoomMe
//  Created by Student2 on 24/03/2022.
//

import SwiftUI

struct ContentView: View {
    @State private var push = 0

    var body: some View {
        
        /*
         0 - login
         1 - main menu
         2 - register
         3 - forgot password
         */
        
        ZStack {
            if push == 0 {
                LoginView(push: $push)
                    .transition(.asymmetric(insertion: .move(edge: .leading), removal: .move(edge: .leading)))
            }

            else if push == 1 {
                MainMenuView(push: $push)
                    .transition(.asymmetric(insertion: .move(edge: .trailing), removal: .move(edge: .trailing)))
            }
            
            else if push == 2 {
                RegisterView(push: $push)
                    .transition(.asymmetric(insertion: .move(edge: .trailing), removal: .move(edge: .trailing)))
            }
            
            else if push == 3 {
                ForgotPasswordView(push: $push)
                    .transition(.asymmetric(insertion: .move(edge: .trailing), removal: .move(edge: .trailing)))
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
  static var previews: some View {
    ContentView()
  }
}
