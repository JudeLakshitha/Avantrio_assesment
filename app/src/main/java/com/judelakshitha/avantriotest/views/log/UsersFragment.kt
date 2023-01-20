package com.judelakshitha.avantriotest.views.log


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.*
import com.android.volley.toolbox.*
import com.judelakshitha.avantriotest.R
import com.judelakshitha.avantriotest.adapters.UserAdapter
import com.judelakshitha.avantriotest.common.CommonHelper
import com.judelakshitha.avantriotest.models.User
import com.judelakshitha.avantriotest.views.home.HomeActivity
import com.judelakshitha.avantriotest.views.login.LoginActivity
import org.json.JSONArray
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.collections.HashMap


class UsersFragment : Fragment() {

    lateinit var userListView: ListView
    lateinit var etSearchUsers: EditText
    lateinit var userAdapter: UserAdapter

    var userNameString = ""
    var token = ""
    var responseArrayList = ArrayList<User>()
    var usersArrayList = ArrayList<User>()
    var filteredDataList = ArrayList<User>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val customView = layoutInflater.inflate(R.layout.fragment_users, null)
        setHasOptionsMenu(true)

        userListView = customView.findViewById(R.id.lv_user_list)
        etSearchUsers = customView.findViewById(R.id.et_search)

        val appSharedData =
            (activity as HomeActivity).getSharedPreferences("logPref", Context.MODE_PRIVATE)
        token = appSharedData.getString("token", "")!!

        getUsers()

        etSearchUsers.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                userNameString = cs.toString()
                filterData(userNameString)
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}

            override fun afterTextChanged(arg0: Editable) {}
        })

        userListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, UserLogsActivity::class.java)
            val bundle = Bundle()

            if (filteredDataList.isEmpty()) {
                bundle.putString("id", usersArrayList[position].id)
                bundle.putString("userName", usersArrayList[position].name)
                intent.putExtras(bundle)
                startActivity(intent)
            } else {
                bundle.putString("id", filteredDataList[position].id)
                bundle.putString("userName", filteredDataList[position].name)
                intent.putExtras(bundle)
                startActivity(intent)
            }

        }

        return customView
    }

    private fun getUsers() {

        responseArrayList.clear()
        val url = "http://apps.avantrio.xyz:8010/api/users"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val request: JsonArrayRequest =
            object : JsonArrayRequest(Method.GET, url, null, Response.Listener { response ->
                Log.i("onResponse", response.toString())
                Log.i("onResponse", response.length().toString());

                try {
                    for (i in 0 until response.length()) {
                        val jsonObj = response.getJSONObject(i)
                        var userObj = User()
                        userObj.id = jsonObj.getString("id")
                        userObj.name = jsonObj.getString("name")
                        userObj.letter = jsonObj.getString("name").substring(0, 1)
                            .uppercase()
                        responseArrayList.add(userObj)
                    }

                    try {
                        usersArrayList = responseArrayList
                        userAdapter = UserAdapter(requireContext(), usersArrayList)
                        userListView.adapter = userAdapter
                        userAdapter.notifyDataSetChanged()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }, Response.ErrorListener { error ->

                Log.e("onErrorResponse", error.toString())
                var errorMessage = ""
                if (error is NetworkError) {
                    errorMessage = "Cannot connect to Service.\nPlease check your \nconnection!"

                } else if (error is ServerError) {
                    errorMessage =
                        "Server could not \nbe found. Please try again \nafter some time!"

                } else if (error is AuthFailureError) {
                    errorMessage = "Authentication Fail!"
                    CommonHelper().clearSharedPref(requireContext(), "logPref")
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()

                } else {
                    errorMessage = "Service is not working.\nPlease try again!"
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

            }) {
                override fun getHeaders(): Map<String, String> {
                    Log.d("dddddddddddddddd", "token " + token);
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }

                override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONArray>? {
                    if (response != null) {
                        if (response.statusCode != 200) {
                            return Response.error(ParseError(response))
                        } else {
                            try {
                                if (response.data.isEmpty()) {
                                    val responseData = "{}".toByteArray(charset("UTF8"))
                                    //response = NetworkResponse(response.statusCode, responseData, response.headers, response.notModified)
                                }
                            } catch (e: UnsupportedEncodingException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    return super.parseNetworkResponse(response)
                }
            }
        requestQueue.add(request)
    }

    fun filterData(constraint: String?) {
        var results = ArrayList<User>()
        val mStringFilterList = usersArrayList

        if (constraint != null && constraint.isNotEmpty()) {
            var filterList = ArrayList<User>()

            for (i in 0 until mStringFilterList.size) {
                var userName = ""
                userName = constraint[0].toString()

                if (userName != "") {
                    if (mStringFilterList[i].name.lowercase(Locale.getDefault())
                            .contains(userName.lowercase(Locale.getDefault()))
                    ) {
                        val user = User()
                        user.name = mStringFilterList[i].name
                        user.id = mStringFilterList[i].id
                        filterList.add(user)

                    }

                } else {
                    filterList = mStringFilterList
                }

            }

            results = filterList

        } else {
            results = usersArrayList
        }

        filteredDataList = results
        userAdapter = UserAdapter(requireContext(), results)
        userListView.adapter = userAdapter
        userAdapter.notifyDataSetChanged()

    }


}

