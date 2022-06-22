//
//  FriendsView.swift
//  RoomMe
//
//  Created by Student2 on 17/05/2022.
//

import SwiftUI

struct BestFriend: Identifiable {
    var id = UUID()
    var description: String
}

struct BestFriendRow: View {
    var task: BestFriend
    var body: some View {
        HStack{
            Image(systemName: "person.crop.circle.badge.plus")
                .foregroundColor(.gray)
                .imageScale(.large)
                Text("\(task.description)")
            .foregroundColor(.gray)
            .font(.headline)
        }
    }
}

var bestfriend1 = BestFriend(description: "smerf gapcio")
var tasksGlobal = [bestfriend1]

struct FriendsView: View{
    @Binding var sceneNumber: Int
    @State var tasks = [bestfriend1]
    var body: some View {
        VStack{
            HStack {
            List(tasks) {
                task in BestFriendRow(task: task)
            }}
            NavigationView {
                VStack{
                    Spacer()
                    HStack {
                        Spacer()
                        NavigationLink(destination: NewFrienfView(tasks: $tasks), label: {
                    Image(systemName: "person.crop.circle.badge.plus")
                        .imageScale(.large)
                }
                )
                    .frame(width: 60, height: 60)
                    .background(Color.green)
                    .foregroundColor(.white)
                    .cornerRadius(90)
                    }
                }
           
    }
        }
    }
}
