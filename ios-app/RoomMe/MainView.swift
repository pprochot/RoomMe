//
//  MainView.swift
//  RoomMe
//
//  Created by Student2 on 22/04/2022.
//

import SwiftUI

struct MainView: View {
    @Binding var showMenu: Bool
    @Binding var sceneNumber: Int
    
    var body: some View {
        
        /*
         0 - Home
         1 - Profile
         2 - Friends
         */
        
        if sceneNumber == 0 {
            HomeView(sceneNumber: $sceneNumber)
        }
        else if sceneNumber == 1 {
           ProfileView()
        }
        else if sceneNumber == 2 {
            FriendsView(sceneNumber: $sceneNumber)
        }
    }
}


