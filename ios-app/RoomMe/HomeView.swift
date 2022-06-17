//
//  HomeView.swift
//  RoomMe
//
//  Created by Student2 on 17/05/2022.
//

import SwiftUI

struct HomeView: View {
    @State var selection = 1
    @Binding var sceneNumber: Int
    var body: some View {
        TabView(selection:$selection) {
           
           ListView()
               .tabItem() {
                   Image(systemName: "list.dash")
                       .foregroundColor(.gray)
               }
           
           TaskView(sceneNumber: $sceneNumber)
               .tabItem() {
                   Image(systemName: "doc.text")
                       .foregroundColor(.gray)
                       
                       
                       }
           
           LittleHomeView(sceneNumber: $sceneNumber)
               .tabItem() {
                   Image(systemName: "house")
                       .foregroundColor(.gray)
                       
            }
               .tag(1)
           
            LittleFriendsView(sceneNumber: $sceneNumber)
                .tabItem() {
                    Image(systemName: "person.2")
                        .foregroundColor(.gray)
            }
           
           StatsView()
               .tabItem() {
                   Image(systemName: "chart.bar")
                       .foregroundColor(.gray)
           }
           
           
        }
    }
}



struct ViewB: View {
    var body: some View {
        Text("Profile2")
    }
}
